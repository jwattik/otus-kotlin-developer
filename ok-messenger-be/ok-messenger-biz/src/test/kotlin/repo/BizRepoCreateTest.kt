package ru.otus.messenger.biz.repo

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
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

class BizRepoCreateTest {

    private val userId = ChatOwnerId("321")
    private val command = ChatCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = ChatRepositoryMock(
        invokeCreateChat = {
            DbChatResponseOk(
                data = MessengerChat(
                    id = ChatId(uuid),
                    title = it.chat.title,
                    description = it.chat.description,
                    ownerId = userId,
                    type = it.chat.type,
                    mode = it.chat.mode,
                )
            )
        }
    )
    private val settings = MessengerCorSettings(
        repoTest = repo
    )
    private val processor = MessengerProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = MessengerContext(
            command = command,
            state = ChatState.NONE,
            workMode = WorkMode.TEST,
            chatRequest = MessengerChat(
                title = "abc",
                description = "abc",
                type = ChatType.GROUP,
                mode = ChatMode.WORK,
            ),
        )
        processor.exec(ctx)
        assertEquals(ChatState.FINISHING, ctx.state)
        assertNotEquals(ChatId.NONE, ctx.chatResponse.id)
        assertEquals("abc", ctx.chatResponse.title)
        assertEquals("abc", ctx.chatResponse.description)
        assertEquals(ChatType.GROUP, ctx.chatResponse.type)
        assertEquals(ChatMode.WORK, ctx.chatResponse.mode)
    }
}
