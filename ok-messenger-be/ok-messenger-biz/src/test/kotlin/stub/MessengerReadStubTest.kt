package ru.otus.messenger.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.stubs.MessengerChatStub

class MessengerReadStubTest {
    private val processor = MessengerProcessor()

    @Test
    fun read() = runTest {

        val ctx = MessengerContext(
            command = ChatCommand.READ,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.SUCCESS,
        )
        processor.exec(ctx)
        with (MessengerChatStub.get()) {
            assertEquals(id, ctx.chatResponse.id)
            assertEquals(title, ctx.chatResponse.title)
            assertEquals(description, ctx.chatResponse.description)
            assertEquals(type, ctx.chatResponse.type)
            assertEquals(mode, ctx.chatResponse.mode)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.READ,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_ID,
        )
        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.READ,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.DB_ERROR,
        )
        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.READ,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_TITLE,
        )
        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}