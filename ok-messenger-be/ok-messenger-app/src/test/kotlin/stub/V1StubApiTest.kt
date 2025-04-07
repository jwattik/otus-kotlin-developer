package ru.otus.messenger.app.stub

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import java.util.UUID
import ru.otus.messenger.api.v1.models.ChatCreateRequest
import ru.otus.messenger.api.v1.models.ChatCreateRequestAllOfChat
import ru.otus.messenger.api.v1.models.ChatCreateResponse
import ru.otus.messenger.api.v1.models.ChatDeleteRequest
import ru.otus.messenger.api.v1.models.ChatDeleteResponse
import ru.otus.messenger.api.v1.models.ChatReadRequest
import ru.otus.messenger.api.v1.models.ChatReadResponse
import ru.otus.messenger.api.v1.models.ChatSearchRequest
import ru.otus.messenger.api.v1.models.ChatSearchRequestAllOfCriteria
import ru.otus.messenger.api.v1.models.ChatSearchResponse
import ru.otus.messenger.api.v1.models.Debug
import ru.otus.messenger.api.v1.models.DebugMode
import ru.otus.messenger.api.v1.models.DebugStubs
import ru.otus.messenger.api.v1.models.IRequest
import ru.otus.messenger.api.v1.models.ResponseResult
import ru.otus.messenger.app.common.MessengerAppSettingsData
import ru.otus.messenger.app.module
import ru.otus.messenger.common.MessengerCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import ru.otus.messenger.stubs.MessengerChatStubSample

class V1StubApiTest {
    private val chatId = UUID.randomUUID().toString()
    private val participants = (MessengerChatStubSample.participants.map { it.asString() } + MessengerChatStubSample.chatOwnerId).toSet()

    @Test
    fun create() = v1TestApplication(
        func = "create",
        request = ChatCreateRequest(
            chat = ChatCreateRequestAllOfChat(
                title = "New chat",
                description = "New chat description",
            ),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<ChatCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(MessengerChatStubSample.chatOwnerId, responseObj.chat?.ownerId)
        assertEquals("New chat", responseObj.chat?.title)
        assertEquals(participants, responseObj.chat?.participants)
    }

    @Test
    fun read() = v1TestApplication(
        func = "read",
        request = ChatReadRequest(
            chatId = MessengerChatStubSample.chatId,
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<ChatReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(MessengerChatStubSample.chatId, responseObj.chat?.id)
    }

    @Test
    fun delete() = v1TestApplication(
        func = "delete",
        request = ChatDeleteRequest(
            chatId = chatId,
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<ChatDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(ResponseResult.SUCCESS, responseObj.result)
    }

    @Test
    fun search() = v1TestApplication(
        func = "search",
        request = ChatSearchRequest(
            criteria = ChatSearchRequestAllOfCriteria(
                title = "Chat search title",
                type = ChatSearchRequestAllOfCriteria.Type.GROUP,
            ),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<ChatSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals(2, responseObj.chats?.size)
        assertEquals("Chat search title", responseObj.chats?.first()?.title)
    }

    private fun v1TestApplication(
        func: String,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(MessengerAppSettingsData(corSettings = MessengerCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/v1/chat/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}
