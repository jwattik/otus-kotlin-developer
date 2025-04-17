package ru.otus.messenger.repo.clickhouse

import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.Test
import ru.otus.messenger.common.repo.DbChatFilterRequest
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatRequest
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.stubs.MessengerChatStub

class ChatRepoClickHouseTest {
    private val repo = mockk<ChatRepoClickHouse>()
    private val stub = MessengerChatStub.get()

    @Test
    fun testCreate() {
        coEvery { repo.createChat(DbChatRequest(stub)) } returns DbChatResponseOk(stub)
    }

    @Test
    fun testRead() {
        coEvery { repo.readChat(DbChatIdRequest(stub.id)) } returns DbChatResponseOk(stub)
    }

    @Test
    fun testDelete() {
        coEvery { repo.deleteChat(DbChatIdRequest(stub.id)) } returns DbChatResponseOk(stub)
    }

    @Test
    fun testSearch() {
        coEvery { repo.searchChat(DbChatFilterRequest()) } returns DbChatsResponseOk(listOf(stub))
    }
}