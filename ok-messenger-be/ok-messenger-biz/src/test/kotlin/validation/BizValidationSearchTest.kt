package ru.otus.messenger.biz.validation

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatSearchFilter
import ru.otus.messenger.common.models.ChatSearchFilter.StringSearchField
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.WorkMode

class BizValidationSearchTest : BaseBizValidationTest() {
    override val command: ChatCommand = ChatCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = MessengerContext(
            command = command,
            state = ChatState.NONE,
            workMode = WorkMode.TEST,
            chatFilterRequest = ChatSearchFilter(
                searchFields = listOf(
                    StringSearchField(
                        fieldName = "",
                        stringValue = ""
                    )
                ),
            )
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(ChatState.FAILING, ctx.state)
    }
}