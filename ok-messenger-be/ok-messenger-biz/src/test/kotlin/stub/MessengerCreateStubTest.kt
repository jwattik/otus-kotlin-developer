package ru.otus.messenger.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.stubs.MessengerChatStub

class MessengerCreateStubTest {
    private val processor = MessengerProcessor()
    private val timestamp = Instant.parse("2025-01-31T06:30:00Z")
    private val content = buildJsonObject {
        put("sampleId", "uuid4")
        put("testParam", "test")
    }

    @Test
    fun create() = runTest {
        val context = MessengerContext(
            command = ChatCommand.CREATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.SUCCESS,
        )
        processor.exec(context)
        assertEquals(MessengerChatStub.get().title, context.chatResponse.title)
        assertEquals(timestamp, context.chatResponse.createdAt)
        assertEquals(content.toString(), context.chatResponse.metadata.asString())
    }

    @Test
    fun badTitle() = runTest {
        val context = MessengerContext(
            command = ChatCommand.CREATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_TITLE,
        )
        processor.exec(context)
        assertEquals(MessengerChat(), context.chatResponse)
        assertEquals("title", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badDescription() = runTest {
        val context = MessengerContext(
            command = ChatCommand.CREATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_DESCRIPTION,
        )
        processor.exec(context)
        assertEquals(MessengerChat(), context.chatResponse)
        assertEquals("description", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val context = MessengerContext(
            command = ChatCommand.CREATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.DB_ERROR,
        )
        processor.exec(context)
        assertEquals(MessengerChat(), context.chatResponse)
        assertEquals("internal", context.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val context = MessengerContext(
            command = ChatCommand.CREATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.NOT_FOUND,
        )
        processor.exec(context)
        assertEquals(MessengerChat(), context.chatResponse)
        assertEquals("stub", context.errors.firstOrNull()?.field)
        assertEquals("validation", context.errors.firstOrNull()?.group)
    }
}