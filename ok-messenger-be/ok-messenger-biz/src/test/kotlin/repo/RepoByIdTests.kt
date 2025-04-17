package ru.otus.messenger.biz.repo

import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.test.runTest
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.errorNotFound
import ru.otus.messenger.repo.tests.ChatRepositoryMock

private val initAd = MessengerChat(
    id = ChatId("123"),
    title = "abc",
    description = "abc",
    type = ChatType.GROUP,
    mode = ChatMode.WORK,
)
private val repo = ChatRepositoryMock(
    invokeReadChat = {
        if (it.id == initAd.id) {
            DbChatResponseOk(
                data = initAd,
            )
        } else errorNotFound(it.id)
    }
)
private val settings = MessengerCorSettings(repoTest = repo)
private val processor = MessengerProcessor(settings)

fun repoNotFoundTest(command: ChatCommand) = runTest {
    val ctx = MessengerContext(
        command = command,
        state = ChatState.NONE,
        workMode = WorkMode.TEST,
        chatRequest = MessengerChat(
            id = ChatId("12345"),
            title = "xyz",
            description = "xyz",
            type = ChatType.GROUP,
            mode = ChatMode.WORK,
        ),
    )
    processor.exec(ctx)
    assertEquals(ChatState.FAILING, ctx.state)
    assertEquals(MessengerChat(), ctx.chatResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
