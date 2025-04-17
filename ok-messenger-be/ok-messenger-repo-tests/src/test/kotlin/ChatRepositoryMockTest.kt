package ru.otus.messenger.repo.tests

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.test.runTest
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.DbChatFilterRequest
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatRequest
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.stubs.MessengerChatStub

class ChatRepositoryMockTest {
    private val repo = ChatRepositoryMock(
        invokeCreateChat = { DbChatResponseOk(MessengerChatStub.prepareResult { title = "create" }) },
        invokeReadChat = { DbChatResponseOk(MessengerChatStub.prepareResult { title = "read" }) },
        invokeUpdateChat = { DbChatResponseOk(MessengerChatStub.prepareResult { title = "update" }) },
        invokeDeleteChat = { DbChatResponseOk(MessengerChatStub.prepareResult { title = "delete" }) },
        invokeSearchChat = { DbChatsResponseOk(listOf(MessengerChatStub.prepareResult { title = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createChat(DbChatRequest(MessengerChat()))
        assertIs<DbChatResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readChat(DbChatIdRequest(MessengerChat()))
        assertIs<DbChatResponseOk>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateChat(DbChatRequest(MessengerChat()))
        assertIs<DbChatResponseOk>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteChat(DbChatIdRequest(MessengerChat()))
        assertIs<DbChatResponseOk>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchChat(DbChatFilterRequest())
        assertIs<DbChatsResponseOk>(result)
        assertEquals("search", result.data.first().title)
    }
}
