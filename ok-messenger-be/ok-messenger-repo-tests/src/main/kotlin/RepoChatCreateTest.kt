package ru.otus.messenger.repo.tests

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlinx.datetime.Instant
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.messenger.common.models.ChatArchiveFlag
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMetadata
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.DbChatRequest
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.repo.common.IRepoChatInitializable

abstract class RepoChatCreateTest {
    abstract val repo: IRepoChatInitializable
    protected open val uuidNew = ChatId("10000000-0000-0000-0000-000000000001")

    private val createObj = MessengerChat(
        id = ChatId("Test"),
        title = "",
        description = "",
        type = ChatType.GROUP,
        mode = ChatMode.PERSONAL,
        ownerId = ChatOwnerId("Test123"),
        participants = mutableSetOf(),
        createdAt = Instant.fromEpochMilliseconds(123456),
        updatedAt = Instant.fromEpochMilliseconds(123456),
        isArchived = ChatArchiveFlag.NONE,
        metadata = ChatMetadata(
            buildJsonObject {
                put("sampleId", "test")
                put("case", "create object")
                put("info", "Why should I repeat this initialization one more time???")
            }
        )
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createChat(DbChatRequest(createObj))
        val expected = createObj
        assertIs<DbChatResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.ownerId, result.data.ownerId)
        assertEquals(expected.type, result.data.type)
        assertEquals(expected.mode, result.data.mode)
        assertEquals(expected.createdAt, result.data.createdAt)
        assertNotEquals(ChatId.NONE, result.data.id)
    }

    companion object : BaseInitChats("create") {
        override val initObjects: List<MessengerChat> = emptyList()
    }
}
