package ru.otus.messenger.api.v1

import java.time.Instant
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import ru.otus.messenger.api.v1.models.*

class ResponseV1SerializationTest {
    private val response = ChatCreateResponse(
        responseType = "create",
        result = ResponseResult.SUCCESS,
        chat = Chat(
            id = UUID.randomUUID().toString(),
            title = "Test chat title",
            type = Chat.Type.CHANNEL,
            mode = Chat.Mode.WORK,
            participants = emptyList(),
            createdAt = Instant.now().toString(),
            isArchived = false,
            metadata = null
        )
    )

    @Test
    fun serialize() {
        val json = apiV1ResponseSerialize(response)

        assertContains(json, Regex("\"title\":\\s*\"${response.chat?.title}\""))
        assertContains(json, Regex("\"id\":\\s*\"${response.chat?.id}\""))
        assertContains(json, Regex("\"responseType\":\\s*\"${response.responseType}\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1ResponseSerialize(response)
        val obj = apiV1ResponseDeserialize<ChatCreateResponse>(json)

        assertEquals(response, obj)
    }
}