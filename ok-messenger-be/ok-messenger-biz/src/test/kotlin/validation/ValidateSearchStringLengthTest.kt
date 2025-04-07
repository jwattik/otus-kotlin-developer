package ru.otus.messenger.biz.validation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatSearchFilter
import ru.otus.messenger.common.models.ChatSearchFilter.StringSearchField
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.cor.dsl.rootChain

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MessengerContext(
            state = ChatState.RUNNING,
            chatFilterValidating = ChatSearchFilter(
                searchFields = listOf(
                    StringSearchField(
                        fieldName = "title",
                        stringValue = "",
                    )
                )
            )
        )
        chain.exec(ctx)
        assertEquals(ChatState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = MessengerContext(
            state = ChatState.RUNNING,
            chatFilterValidating = ChatSearchFilter(
                searchFields = listOf(
                    StringSearchField(
                        fieldName = "title",
                        stringValue = "   ",
                    )
                )
            )
        )
        chain.exec(ctx)
        assertEquals(ChatState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = MessengerContext(
            state = ChatState.RUNNING,
            chatFilterValidating = ChatSearchFilter(
                searchFields = listOf(
                    StringSearchField(
                        fieldName = "title",
                        stringValue = "12",
                    )
                )
            )
        )
        chain.exec(ctx)
        assertEquals(ChatState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MessengerContext(
            state = ChatState.RUNNING,
            chatFilterValidating = ChatSearchFilter(
                searchFields = listOf(
                    StringSearchField(
                        fieldName = "title",
                        stringValue = "123",
                    )
                )
            )
        )
        chain.exec(ctx)
        assertEquals(ChatState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = MessengerContext(
            state = ChatState.RUNNING,
            chatFilterValidating = ChatSearchFilter(
                searchFields = listOf(
                    StringSearchField(
                        fieldName = "title",
                        stringValue = "12".repeat(51),
                    )
                )
            )
        )
        chain.exec(ctx)
        assertEquals(ChatState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}