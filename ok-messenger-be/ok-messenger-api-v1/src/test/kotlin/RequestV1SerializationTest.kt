package ru.otus.messenger.api.v1

import ru.otus.messenger.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = ChatCreateRequest(
        debug = Debug(
            mode = DebugMode.STUB,
            stub = DebugStubs.VALUE_ERROR
        ),
        requestType = "create",
        chat = ChatCreateRequestAllOfChat(
            title = "New chat",
            type = ChatCreateRequestAllOfChat.Type.GROUP,
            mode = ChatCreateRequestAllOfChat.Mode.PERSONAL,
            participants = emptySet(),
            metadata = """
                {
                    "isOwner": true,
                    "testMeta": "qwerty",
                }
            """.trimIndent()
        )
    )

    @Test
    fun serialize() {
        val json = apiV1RequestSerialize(request)

        assertContains(json, Regex("\"title\":\\s*\"${request.chat?.title}\""))
        assertContains(json, Regex("\"mode\":\\s*\"${request.debug?.mode}\""))
        assertContains(json, Regex("\"stub\":\\s*\"${request.debug?.stub}\""))
        assertContains(json, Regex("\"requestType\":\\s*\"${request.requestType}\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1RequestSerialize(request)
        val obj = apiV1RequestDeserialize<ChatCreateRequest>(json)

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            { "requestType": "create" }
        """.trimIndent()
        val obj = apiV1RequestDeserialize<ChatCreateRequest>(jsonString)

        assertEquals(null, obj.chat)
    }
}