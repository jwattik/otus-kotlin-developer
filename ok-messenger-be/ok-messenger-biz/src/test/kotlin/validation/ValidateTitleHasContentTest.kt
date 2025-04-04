package ru.otus.messenger.biz.validation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatSearchFilter
import ru.otus.messenger.common.models.ChatSearchFilter.StringSearchField
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.cor.dsl.rootChain

class ValidateTitleHasContentTest {
    @Test
    fun emptyString() = runTest {
        val ctx = MessengerContext(state = ChatState.RUNNING, chatValidating = MessengerChat(title = ""))
        chain.exec(ctx)
        assertEquals(ChatState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx =
            MessengerContext(state = ChatState.RUNNING, chatValidating = MessengerChat(title = "12!@#$%^&*()_+-="))
        chain.exec(ctx)
        assertEquals(ChatState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-title-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = MessengerContext(
            state = ChatState.RUNNING,
            chatFilterValidating = ChatSearchFilter(
                searchFields = listOf(
                    StringSearchField(
                        fieldName = "title",
                        stringValue = "Ж"
                    )
                )
            )
        )
        chain.exec(ctx)
        assertEquals(ChatState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateTitleHasContent("")
        }.build()
    }
}