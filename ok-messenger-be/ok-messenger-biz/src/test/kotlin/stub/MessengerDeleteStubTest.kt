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

class MessengerDeleteStubTest {
    private val processor = MessengerProcessor()

    @Test
    fun delete() = runTest {
        val context = MessengerContext(
            command = ChatCommand.DELETE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.SUCCESS,
        )
        processor.exec(context)

        val stub = MessengerChatStub.get()
        assertEquals(stub.id, context.chatResponse.id)
        assertEquals(stub.title, context.chatResponse.title)
        assertEquals(stub.type, context.chatResponse.type)
        assertEquals(stub.mode, context.chatResponse.mode)
    }

    @Test
    fun badNoCase() = runTest {
        val context = MessengerContext(
            command = ChatCommand.DELETE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.NOT_FOUND,
        )
        processor.exec(context)
        assertEquals(MessengerChat(), context.chatResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badId() = runTest {
        val context = MessengerContext(
            command = ChatCommand.DELETE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_ID
        )
        processor.exec(context)
        assertEquals(MessengerChat(), context.chatResponse)
        assertEquals("id", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = MessengerContext(
            command = ChatCommand.DELETE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.DB_ERROR,
        )
        processor.exec(context)
        assertEquals(MessengerChat(), context.chatResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }
}
