package ru.otus.messenger.repo.clickhouse

import com.benasher44.uuid.uuid4
import com.clickhouse.client.api.Client
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import ru.otus.messenger.common.NONE
import ru.otus.messenger.common.models.ChatArchiveFlag
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMetadata
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatSearchFilter
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.ChatRepoBase
import ru.otus.messenger.common.repo.DbChatFilterRequest
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatRequest
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.common.repo.IDbChatResponse
import ru.otus.messenger.common.repo.IDbChatsResponse
import ru.otus.messenger.common.repo.IRepoChat
import ru.otus.messenger.common.repo.errorEmptyId
import ru.otus.messenger.common.repo.errorNotFound
import ru.otus.messenger.repo.common.IRepoChatInitializable

class ChatRepoClickHouse(
    properties: DbProperties,
    private val randomUuid: () -> String = { uuid4().toString() }
) : ChatRepoBase(), IRepoChat, IRepoChatInitializable {

    private val chatTable: String = "chat"

    private val client: Client = Client.Builder()
        .addEndpoint("https://${properties.host}:${properties.port}/")
        .setUsername(properties.user)
        .setPassword(properties.password)
        .build().also {
            it.register(ChatTableRecord::class.java, it.getTableSchema(chatTable))
        }

    override fun save(chats: Collection<MessengerChat>): Collection<MessengerChat> {
        try {
            client.insert(
                chatTable,
                chats.map { chat -> ChatTableRecord(chat) }
            )
            return chats
        } catch (e: Exception) {
            return listOf()
        }
    }

    override suspend fun createChat(request: DbChatRequest): IDbChatResponse = tryChatMethod {
        val key = randomUuid()
        val chat = request.chat.copy(id = ChatId(key))
        val record = ChatTableRecord(chat)
        withContext(Dispatchers.IO) {
            client.insert(chatTable, listOf(record))
        }
        DbChatResponseOk(chat)
    }

    override suspend fun readChat(request: DbChatIdRequest): IDbChatResponse = tryChatMethod {
        val key = request.id.takeIf { it != ChatId.NONE }?.asString() ?: return@tryChatMethod errorEmptyId

        val sql = "SELECT * FROM $chatTable WHERE chatId = $key"

        // Default format is RowBinaryWithNamesAndTypesFormatReader so reader have all information about columns
        withContext(Dispatchers.IO) {
            client.query(sql)[3, TimeUnit.SECONDS].use { response ->

                // Create a reader to access the data in a convenient way
                val reader = client.newBinaryFormatReader(response)
                if (reader.hasNext()) {
                    // Read the next record from stream and parse it
                    reader.next()

                    // get values
                    DbChatResponseOk(
                        ChatTableRecord(
                            chatId = reader.getString("chatId"),
                            title = reader.getString("title"),
                            description = reader.getString("description"),
                            type = reader.getString("type"),
                            mode = reader.getString("mode"),
                            ownerId = reader.getString("ownerId"),
                            participants = reader.getList("participants"),
                            createdAt = reader.getInstant("createdAt").toKotlinInstant(),
                            updatedAt = reader.getInstant("updatedAt").toKotlinInstant(),
                            isArchived = reader.getBoolean("isArchived"),
                            metadata = reader.getString("metadata"),
                        ).toInternal()
                    )
                } else {
                    errorNotFound(request.id)
                }
            }
        }
    }

    override suspend fun updateChat(request: DbChatRequest): IDbChatResponse = tryChatMethod {
        val chatId = request.chat.id.takeIf { it != ChatId.NONE } ?: return@tryChatMethod errorEmptyId
        val chat = request.chat

        val updates = mutableListOf<String>()
        val params = mutableMapOf<String, Any>()

        if (chat.title.isNotBlank()) {
            updates += "title = :title"
            params["title"] = chat.title
        }
        if (chat.description.isNotBlank()) {
            updates += "description = :description"
            params["description"] = chat.description
        }
        if (chat.type != ChatType.NONE) {
            updates += "chat_type = :chatType"
            params["chatType"] = chat.type.name
        }
        if (chat.mode != ChatMode.NONE) {
            updates += "chat_mode = :chatMode"
            params["chatMode"] = chat.mode.name
        }
        if (chat.ownerId != ChatOwnerId.NONE) {
            updates += "owner_id = :ownerId"
            params["ownerId"] = chat.ownerId.asString()
        }
        if (chat.createdAt != Instant.NONE) {
            updates += "created_at = :createdAt"
            params["createdAt"] = chat.createdAt
        }
        if (chat.updatedAt != Instant.NONE) {
            updates += "updated_at = :updatedAt"
            params["updatedAt"] = chat.updatedAt
        }
        if (chat.isArchived != ChatArchiveFlag.NONE) {
            updates += "is_archived = :isArchived"
            params["isArchived"] = chat.isArchived.asBoolean()
        }
        if (chat.metadata != ChatMetadata.NONE) {
            updates += "metadata = :metadata"
            params["metadata"] = chat.metadata.asString()
        }

        if (updates.isEmpty()) {
            throw IllegalArgumentException("No fields to update")
        }

        params["chatId"] = chatId.asString()

        val sql = """
            UPDATE $chatTable
            SET ${updates.joinToString(", ")}
            WHERE id = :chatId
        """.trimIndent()

        withContext(Dispatchers.IO) {
            client.query(sql)[3, TimeUnit.SECONDS]
        }

        readChat(DbChatIdRequest(chatId))
    }

    override suspend fun deleteChat(request: DbChatIdRequest): IDbChatResponse = tryChatMethod {
        val chatId = request.id.takeIf { it != ChatId.NONE } ?: return@tryChatMethod errorEmptyId
        val key = chatId.asString()

        val result = readChat(request)
        val sql = "DELETE FROM $chatTable WHERE chatId = $key"

        withContext(Dispatchers.IO) {
            client.query(sql)[3, TimeUnit.SECONDS]
        }
        result
    }

    override suspend fun searchChat(request: DbChatFilterRequest): IDbChatsResponse = tryChatsMethod {
        val result: MutableList<ChatTableRecord> = mutableListOf()

        val whereClauses = mutableListOf<String>()
        val params = mutableMapOf<String, Any>()

        if (request.ownerId != ChatOwnerId.NONE) {
            whereClauses += "ownerId = :ownerId"
            params["ownerId"] = request.ownerId.asString()
        }

        if (request.chatType != ChatType.NONE) {
            whereClauses += "chatType = :chatType"
            params["chatType"] = request.chatType.name
        }

        if (request.chatMode != ChatMode.NONE) {
            whereClauses += "chatMode = :chatMode"
            params["chatMode"] = request.chatMode.name
        }

        if (request.searchFields.isNotEmpty()) {
            val searchConditions = request.searchFields.mapIndexed { index, field ->
                field as ChatSearchFilter.StringSearchField
                val paramName = "searchField$index"
                params[paramName] = "%${field.stringValue}%"
                "${field.fieldName} ILIKE :$paramName"
            }
            whereClauses += "(${searchConditions.joinToString(" OR ")})"
        }

        val wherePart = if (whereClauses.isNotEmpty()) {
            "WHERE " + whereClauses.joinToString(" AND ")
        } else {
            ""
        }

        val sql = "SELECT * FROM $chatTable $wherePart"

        // Default format is RowBinaryWithNamesAndTypesFormatReader so reader have all information about columns
        withContext(Dispatchers.IO) {
            client.query(sql)[3, TimeUnit.SECONDS].use { response ->

                // Create a reader to access the data in a convenient way
                val reader = client.newBinaryFormatReader(response)
                while (reader.hasNext()) {
                    // Read the next record from stream and parse it
                    reader.next()

                    // get values
                    result.add(
                        ChatTableRecord(
                            chatId = reader.getString("chatId"),
                            title = reader.getString("title"),
                            description = reader.getString("description"),
                            type = reader.getString("type"),
                            mode = reader.getString("mode"),
                            ownerId = reader.getString("ownerId"),
                            participants = reader.getList("participants"),
                            createdAt = reader.getInstant("createdAt").toKotlinInstant(),
                            updatedAt = reader.getInstant("updatedAt").toKotlinInstant(),
                            isArchived = reader.getBoolean("isArchived"),
                            metadata = reader.getString("metadata"),
                        )
                    )
                }

            }
        }

        DbChatsResponseOk(result.map { it.toInternal() })
    }
}