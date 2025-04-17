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

fun validationDescriptionCorrect(command: ChatCommand, processor: MessengerProcessor) = runBizTest {
    val ctx = MessengerContext(
        command = command,
        state = ChatState.NONE,
        workMode = WorkMode.TEST,
        chatRequest = MessengerChatStub.get(),
    )
    
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(ChatState.FAILING, ctx.state)
    assertContains(ctx.chatValidated.description, "description")
}

fun validationDescriptionTrim(command: ChatCommand, processor: MessengerProcessor) = runBizTest {
    val ctx = MessengerContext(
        command = command,
        state = ChatState.NONE,
        workMode = WorkMode.TEST,
        chatRequest = MessengerChatStub.prepareResult {
            description = " \n\tabc \n\t"
        },
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(ChatState.FAILING, ctx.state)
    assertEquals("abc", ctx.chatValidated.description)
}

fun validationDescriptionEmpty(command: ChatCommand, processor: MessengerProcessor) = runBizTest {
    val ctx = MessengerContext(
        command = command,
        state = ChatState.NONE,
        workMode = WorkMode.TEST,
        chatRequest = MessengerChatStub.prepareResult {
            description = ""
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(ChatState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: ChatCommand, processor: MessengerProcessor) = runBizTest {
    val ctx = MessengerContext(
        command = command,
        state = ChatState.NONE,
        workMode = WorkMode.TEST,
        chatRequest = MessengerChatStub.prepareResult {
            description = "!@#$%^&*(),.{}"
        },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(ChatState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("description", error?.field)
    assertContains(error?.message ?: "", "description")
}
