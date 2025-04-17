package ru.otus.messenger.repo.tests

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertEquals
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatResponseErr
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.IRepoChat

abstract class RepoChatReadTest {
    abstract val repo: IRepoChat
    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readChat(DbChatIdRequest(readSuccess.id))

        assertIs<DbChatResponseOk>(result)
        assertEquals(readSuccess, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readChat(DbChatIdRequest(notFoundId))

        assertIs<DbChatResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitChats("read") {
        override val initObjects: List<MessengerChat> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = ChatId("repo-read-notFound")
    }
}
