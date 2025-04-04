package ru.otus.messenger.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.stubs.MessengerChatStubSample

class MessengerUpdateStubTest {
    private val processor = MessengerProcessor()
    val id = ChatId(MessengerChatStubSample.chatId)
    val title = "New chat"
    val description = "New chat description"
    val type = ChatType.GROUP
    val mode = ChatMode.PERSONAL

    @Test
    fun create() = runTest {

        val ctx = MessengerContext(
            command = ChatCommand.UPDATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.SUCCESS
        )
        
        processor.exec(ctx)
        assertEquals(id, ctx.chatResponse.id)
        assertEquals(title, ctx.chatResponse.title)
        assertEquals(description, ctx.chatResponse.description)
        assertEquals(type, ctx.chatResponse.type)
        assertEquals(mode, ctx.chatResponse.mode)
    }

    @Test
    fun badId() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.UPDATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_ID,
            chatRequest = MessengerChat(),
        )

        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.UPDATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_TITLE,
        )

        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.UPDATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_DESCRIPTION,
        )

        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.UPDATE,
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
            command = ChatCommand.UPDATE,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_SEARCH_STRING,
        )

        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}