package ru.otus.messenger.repo.tests

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.DbChatRequest
import ru.otus.messenger.common.repo.DbChatResponseErr
import ru.otus.messenger.common.repo.DbChatResponseErrWithData
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.IRepoChat

abstract class RepoChatUpdateTest {
    abstract val repo: IRepoChat

    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = ChatId("chat-repo-update-not-found")
    protected val chatIdBad = ChatId("20000000-0000-0000-0000-000000000009")

    protected open val initializedObjects: List<MessengerChat> = initObjects

    private val reqUpdateSucc by lazy {
        MessengerChat(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = updateSucc.ownerId,
            type = updateSucc.type,
            mode = updateSucc.mode,
        )
    }
    private val reqUpdateNotFound = MessengerChat(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = updateConc.ownerId,
        type = updateConc.type,
        mode = updateConc.mode,
    )
    private val reqUpdateConc by lazy {
        MessengerChat(
            id = chatIdBad,
            title = "update object not found",
            description = "update object not found description",
            ownerId = updateConc.ownerId,
            type = updateConc.type,
            mode = updateConc.mode,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateChat(DbChatRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DbChatResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbChatResponseErrWithData)?.errors}")
        assertIs<DbChatResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(reqUpdateSucc.type, result.data.type)
        assertEquals(reqUpdateSucc.mode, result.data.mode)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateChat(DbChatRequest(reqUpdateNotFound))
        assertIs<DbChatResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateChat(DbChatRequest(reqUpdateConc))
        println(result)
        assertIs<DbChatResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object: BaseInitChats("update") {
        override val initObjects: List<MessengerChat> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}