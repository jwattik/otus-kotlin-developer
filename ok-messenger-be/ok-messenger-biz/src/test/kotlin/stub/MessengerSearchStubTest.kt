package ru.otus.messenger.biz.stub

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlinx.coroutines.test.runTest
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatSearchFilter
import ru.otus.messenger.common.models.ChatSearchFilter.StringSearchField
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.stubs.MessengerChatStub

class MessengerSearchStubTest {
    private val processor = MessengerProcessor()
    val filter = ChatSearchFilter(
        searchFields = listOf(
            StringSearchField(fieldName = "title", stringValue = "New chat" ),
            StringSearchField(fieldName = "description", stringValue = "New chat description" )
        )
    )

    @Test
    fun read() = runTest {

        val ctx = MessengerContext(
            command = ChatCommand.SEARCH,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.SUCCESS,
            chatFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.chatsResponse.size > 1)
        val first = ctx.chatsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains((filter.searchFields[0] as StringSearchField).stringValue))
        assertTrue(first.description.contains((filter.searchFields[1] as StringSearchField).stringValue))
        with(MessengerChatStub.get()) {
            assertEquals(type, first.type)
            assertEquals(mode, first.mode)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.SEARCH,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_ID,
            chatFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.SEARCH,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.DB_ERROR,
            chatFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = MessengerContext(
            command = ChatCommand.SEARCH,
            state = ChatState.NONE,
            workMode = WorkMode.STUB,
            stubCase = MessengerStubs.BAD_TITLE,
            chatFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(MessengerChat(), ctx.chatResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}