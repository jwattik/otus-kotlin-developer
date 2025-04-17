package ru.otus.messenger.repo.tests

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatSearchFilter
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.DbChatFilterRequest
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.common.repo.IRepoChat

abstract class RepoChatSearchTest {
    abstract val repo: IRepoChat

    protected open val initializedObjects: List<MessengerChat> = initObjects

    @Test
    fun searchByOwnerId() = runRepoTest {
        val result = repo.searchChat(
            DbChatFilterRequest(ownerId = ChatOwnerId("TestOwnerId"))
        )
        assertIs<DbChatsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchByTypeAndMode() = runRepoTest {
        val result = repo.searchChat(
            DbChatFilterRequest(
                chatType = ChatType.GROUP,
                chatMode = ChatMode.WORK,
            )
        )
        assertIs<DbChatsResponseOk>(result)
        val expected = listOf(initializedObjects[0]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchContent() = runRepoTest {
        val result = repo.searchChat(
            DbChatFilterRequest(
                searchFields = listOf(
                    ChatSearchFilter.StringSearchField(
                        fieldName = "ownerId",
                        action = ChatSearchFilter.SearchAction.EQUALS,
                        stringValue = "Hmm",
                    )
                )
            )
        )
        assertIs<DbChatsResponseOk>(result)
        val expected = listOf(initializedObjects[0], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitChats("search") {
        private val searchOwnerId = ChatOwnerId("TestOwnerId")
        private val searchType = ChatType.CHANNEL
        private val searchMode = ChatMode.PERSONAL
        override val initObjects: List<MessengerChat> = listOf(
            createInitTestModel(
                "test1",
                chatOwnerId = ChatOwnerId("Hmm"),
                chatType = ChatType.GROUP,
                chatMode = ChatMode.WORK,
            ),
            createInitTestModel(
                "test2",
                chatOwnerId = searchOwnerId,
                chatType = searchType,
                chatMode = searchMode,
            ),
            createInitTestModel(
                "test3",
                chatOwnerId = ChatOwnerId("Eee!"),
                chatType = searchType,
                chatMode = ChatMode.WORK,
            ),
            createInitTestModel(
                "test4",
                chatOwnerId = searchOwnerId,
                chatType = ChatType.PRIVATE
            ),
            createInitTestModel(
                "test5",
                chatOwnerId = ChatOwnerId("Hmm"),
                chatType = ChatType.PRIVATE,
                chatMode = ChatMode.WORK,
            ),
        )
    }
}
