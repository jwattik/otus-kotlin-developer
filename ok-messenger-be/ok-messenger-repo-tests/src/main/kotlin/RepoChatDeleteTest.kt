package ru.otus.messenger.repo.tests

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatResponseErr
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.IRepoChat

abstract class RepoChatDeleteTest {
    abstract val repo: IRepoChat

    protected open val deleteSuccess = initObjects[0]
    protected open val notFoundId = ChatId("repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteChat(DbChatIdRequest(deleteSuccess.id))
        assertIs<DbChatResponseOk>(result)
        assertEquals(deleteSuccess.ownerId, result.data.ownerId)
        assertEquals(deleteSuccess.type, result.data.type)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteChat(DbChatIdRequest(notFoundId))

        assertIs<DbChatResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    companion object : BaseInitChats("delete") {
        override val initObjects: List<MessengerChat> = listOf(
            createInitTestModel("delete"),
        )
    }
}
