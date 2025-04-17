package ru.otus.messenger.biz.repo

import kotlin.test.assertEquals
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
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.repo.tests.ChatRepositoryMock

class BizRepoUpdateTest {

    private val userId = ChatOwnerId("321")
    private val command = ChatCommand.UPDATE
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
        invokeUpdateChat = {
            DbChatResponseOk(
                data = MessengerChat(
                    id = ChatId("123"),
                    title = "xyz",
                    description = "xyz",
                    type = ChatType.GROUP,
                    mode = ChatMode.WORK,
                )
            )
        }
    )
    private val settings = MessengerCorSettings(repoTest = repo)
    private val processor = MessengerProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val chatToUpdate = MessengerChat(
            id = ChatId("123"),
            title = "xyz",
            description = "xyz",
            type = ChatType.GROUP,
            mode = ChatMode.WORK,
        )
        val ctx = MessengerContext(
            command = command,
            state = ChatState.NONE,
            workMode = WorkMode.TEST,
            chatRequest = chatToUpdate,
        )
        processor.exec(ctx)
        assertEquals(ChatState.FINISHING, ctx.state)
        assertEquals(chatToUpdate.id, ctx.chatResponse.id)
        assertEquals(chatToUpdate.title, ctx.chatResponse.title)
        assertEquals(chatToUpdate.description, ctx.chatResponse.description)
        assertEquals(chatToUpdate.type, ctx.chatResponse.type)
        assertEquals(chatToUpdate.mode, ctx.chatResponse.mode)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
