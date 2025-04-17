package ru.otus.messenger.biz.validation

import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.stubs.MessengerChatStub

fun validationTitleCorrect(command: ChatCommand, processor: MessengerProcessor) = runBizTest {
    val ctx = MessengerContext(
        command = command,
        state = ChatState.NONE,
        workMode = WorkMode.TEST,
        chatRequest = MessengerChatStub.prepareResult {
            title = "abc"
        },
    )
    processor.exec(ctx)
    println(ctx.errors.joinToString("\n"))
    assertEquals(0, ctx.errors.size)
    assertNotEquals(ChatState.FAILING, ctx.state)
    assertEquals("abc", ctx.chatValidated.title)
}

fun validationTitleTrim(command: ChatCommand, processor: MessengerProcessor) = runBizTest {
    val ctx = MessengerContext(
        command = command,
        state = ChatState.NONE,
        workMode = WorkMode.TEST,
        chatRequest = MessengerChatStub.prepareResult {
            title = " \n\t abc \t\n "
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(ChatState.FAILING, ctx.state)
    assertEquals("abc", ctx.chatValidated.title)
}

fun validationTitleEmpty(command: ChatCommand, processor: MessengerProcessor) = runBizTest {
    val ctx = MessengerContext(
        command = command,
        state = ChatState.NONE,
        workMode = WorkMode.TEST,
        chatRequest = MessengerChatStub.prepareResult {
            title = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(ChatState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

fun validationTitleSymbols(command: ChatCommand, processor: MessengerProcessor) = runBizTest {
    val ctx = MessengerContext(
        command = command,
        state = ChatState.NONE,
        workMode = WorkMode.TEST,
        chatRequest = MessengerChatStub.prepareResult {
            title = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(ChatState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
