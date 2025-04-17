package ru.otus.messenger.biz.repo

import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatSearchFilter
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.repo.tests.ChatRepositoryMock

class BizRepoSearchTest {

    private val userId = ChatOwnerId("321")
    private val command = ChatCommand.SEARCH
    private val initAd = MessengerChat(
        id = ChatId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId,
        type = ChatType.GROUP,
        mode = ChatMode.WORK,
    )
    private val repo = ChatRepositoryMock(
        invokeSearchChat = {
            DbChatsResponseOk(
                data = listOf(initAd),
            )
        }
    )
    private val settings = MessengerCorSettings(repoTest = repo)
    private val processor = MessengerProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = MessengerContext(
            command = command,
            state = ChatState.NONE,
            workMode = WorkMode.TEST,
            chatFilterRequest = ChatSearchFilter(
                searchFields = listOf(
                    ChatSearchFilter.StringSearchField(
                        fieldName = "ownerId",
                        stringValue = userId.asString()
                    )
                ),
                type = ChatType.GROUP,
                mode = ChatMode.WORK,
            ),
        )
        processor.exec(ctx)
        assertEquals(ChatState.FINISHING, ctx.state)
        assertEquals(1, ctx.chatsResponse.size)
    }
}
