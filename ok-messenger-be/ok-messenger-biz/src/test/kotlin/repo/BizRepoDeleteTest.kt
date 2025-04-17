package ru.otus.messenger.biz.repo

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.repo.DbChatResponseErr
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.repo.tests.ChatRepositoryMock

class BizRepoDeleteTest {

    private val userId = ChatOwnerId("321")
    private val command = ChatCommand.DELETE
    private val initAd = MessengerChat(
        id = ChatId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        type = ChatType.GROUP,
        mode = ChatMode.WORK,
    )
    private val repo = ChatRepositoryMock(
        invokeReadChat = {
            DbChatResponseOk(
                data = initAd,
            )
        },
        invokeDeleteChat = {
            if (it.id == initAd.id)
                DbChatResponseOk(
                    data = initAd
                )
            else DbChatResponseErr()
        }
    )
    private val settings by lazy {
        MessengerCorSettings(
            repoTest = repo
        )
    }
    private val processor = MessengerProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val chatToUpdate = MessengerChat(
            id = ChatId("123"),
        )
        val ctx = MessengerContext(
            command = command,
            state = ChatState.NONE,
            workMode = WorkMode.TEST,
            chatRequest = chatToUpdate,
        )
        processor.exec(ctx)
        assertEquals(ChatState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initAd.id, ctx.chatResponse.id)
        assertEquals(initAd.title, ctx.chatResponse.title)
        assertEquals(initAd.description, ctx.chatResponse.description)
        assertEquals(initAd.type, ctx.chatResponse.type)
        assertEquals(initAd.mode, ctx.chatResponse.mode)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
