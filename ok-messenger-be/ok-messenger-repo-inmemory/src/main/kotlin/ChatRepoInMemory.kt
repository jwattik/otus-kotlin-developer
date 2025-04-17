package ru.otus.messenger.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.messenger.common.exceptions.RepoEmptyLockException
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatSearchFilter
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.ChatRepoBase
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatRequest
import ru.otus.messenger.common.repo.DbChatFilterRequest
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.common.repo.IDbChatResponse
import ru.otus.messenger.common.repo.IDbChatsResponse
import ru.otus.messenger.common.repo.IRepoChat
import ru.otus.messenger.common.repo.errorDb
import ru.otus.messenger.common.repo.errorEmptyId
import ru.otus.messenger.common.repo.errorNotFound
import ru.otus.messenger.common.repo.errorRepoConcurrency
import ru.otus.messenger.repo.common.IRepoChatInitializable

class ChatRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : ChatRepoBase(), IRepoChat, IRepoChatInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, ChatEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(chats: Collection<MessengerChat>) = chats.map { chat ->
        val entity = ChatEntity(chat)
        require(entity.chatId != null)
        cache.put(entity.chatId, entity)
        chat
    }

    override suspend fun createChat(request: DbChatRequest): IDbChatResponse = tryChatMethod {
        val key = randomUuid()
        val chat = request.chat.copy(id = ChatId(key))
        val entity = ChatEntity(chat)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbChatResponseOk(chat)
    }

    override suspend fun readChat(request: DbChatIdRequest): IDbChatResponse = tryChatMethod {
        val key = request.id.takeIf { it != ChatId.NONE }?.asString() ?: return@tryChatMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbChatResponseOk(it.toInternal())
                } ?: errorNotFound(request.id)
        }
    }

    override suspend fun updateChat(request: DbChatRequest): IDbChatResponse = tryChatMethod {
        val requestChat = request.chat
        val chatId = requestChat.id.takeIf { it != ChatId.NONE } ?: return@tryChatMethod errorEmptyId
        val key = chatId.asString()

        mutex.withLock {
            val oldChat = cache.get(key)?.toInternal()
            when {
                oldChat == null -> errorNotFound(chatId)
                oldChat.id == ChatId.NONE -> errorDb(RepoEmptyLockException(chatId))
                oldChat.id != chatId -> errorRepoConcurrency(oldChat)
                else -> {
                    val newAd = requestChat.copy(id = ChatId(randomUuid()))
                    val entity = ChatEntity(newAd)
                    cache.put(key, entity)
                    DbChatResponseOk(newAd)
                }
            }
        }
    }

    override suspend fun deleteChat(request: DbChatIdRequest): IDbChatResponse = tryChatMethod {
        val chatId = request.id.takeIf { it != ChatId.NONE } ?: return@tryChatMethod errorEmptyId
        val key = chatId.asString()

        mutex.withLock {
            val oldChat = cache.get(key)?.toInternal()
            when {
                oldChat == null -> errorNotFound(chatId)
                oldChat.id == ChatId.NONE -> errorDb(RepoEmptyLockException(chatId))
                oldChat.id != chatId -> errorRepoConcurrency(oldChat)
                else -> {
                    cache.invalidate(key)
                    DbChatResponseOk(oldChat)
                }
            }
        }
    }

    override suspend fun searchChat(request: DbChatFilterRequest): IDbChatsResponse = tryChatsMethod {
        val result: List<MessengerChat> = cache.asMap().asSequence()
            .filter { entry ->
                request.chatType.takeIf { it != ChatType.NONE }?.let {
                    it.name == entry.value.type
                } ?: true
            }
            .filter { entry ->
                request.chatMode.takeIf { it != ChatMode.NONE }?.let {
                    it.name == entry.value.mode
                } ?: true
            }
            .filter { entry ->
                request.ownerId.takeIf { it != ChatOwnerId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                request.searchFields.takeIf { it.isNotEmpty() }?.let { searchFields ->
                    searchFields.any { searchField ->
                        when (searchField) {
                            is ChatSearchFilter.StringSearchField -> {
                                when (searchField.fieldName.lowercase()) {
                                    "chatid" -> assertAction(searchField.stringValue, entry.value.chatId!!, searchField.action)
                                    "title" -> assertAction(searchField.stringValue, entry.value.title!!, searchField.action)
                                    "ownerid" -> assertAction(searchField.stringValue, entry.value.ownerId!!, searchField.action)
                                    "description" -> assertAction(searchField.stringValue, entry.value.description!!, searchField.action)
                                    "isarchived" -> assertAction(searchField.stringValue, entry.value.isArchived.toString(), searchField.action)
                                    "metadata" -> assertAction(searchField.stringValue, entry.value.metadata!!, searchField.action)
                                    else -> false
                                }
                            }

                            else -> false
                        }
                    }
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbChatsResponseOk(result)
    }

    fun assertAction(expected: String, actual: String, action: ChatSearchFilter.SearchAction) =
        when (action) {
            ChatSearchFilter.SearchAction.EQUALS -> expected.toString() == actual
            ChatSearchFilter.SearchAction.CONTAINS -> expected.toString().contains(actual, ignoreCase = true)
            ChatSearchFilter.SearchAction.MORE -> expected > actual
            ChatSearchFilter.SearchAction.LESS -> expected < actual
            else -> false
        }
}
