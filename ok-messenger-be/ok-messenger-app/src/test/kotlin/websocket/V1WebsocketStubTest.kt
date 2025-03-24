package ru.otus.messenger.app.websocket

import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import java.util.UUID
import kotlinx.coroutines.withTimeout
import ru.otus.messenger.api.v1.models.*
import ru.otus.messenger.app.common.MessengerAppSettingsData
import ru.otus.messenger.app.module
import ru.otus.messenger.common.MessengerCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class V1WebsocketStubTest {

    @Test
    fun createStub() {
        val request = ChatCreateRequest(
            chat = ChatCreateRequestAllOfChat(
                title = "New chat title",
                description = "New chat description",
                type = ChatCreateRequestAllOfChat.Type.CHANNEL,
                mode = ChatCreateRequestAllOfChat.Mode.WORK,
                ownerId = UUID.randomUUID().toString(),
                participants = setOf(UUID.randomUUID().toString()),
                metadata = """
                    { 
                        "organization": "BlancLabs",
                        "sampleName": "B26",
                        "analyte": "DNA"
                    }
                """.trimIndent(),
            ),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun readStub() {
        val request = ChatReadRequest(
            chatId = UUID.randomUUID().toString(),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun deleteStub() {
        val request = ChatDeleteRequest(
            chatId = UUID.randomUUID().toString(),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun searchStub() {
        val request = ChatSearchRequest(
            criteria = ChatSearchRequestAllOfCriteria(
                title = "Chat search title",
                type = ChatSearchRequestAllOfCriteria.Type.CHANNEL,
                mode = ChatSearchRequestAllOfCriteria.Mode.WORK,
            ),
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application { module(MessengerAppSettingsData(corSettings = MessengerCorSettings())) }
        val client = createClient {
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter()
            }
        }

        client.webSocket("/v1/ws") {
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertIs<ChatInitResponse>(response)
            }
            sendSerialized(request)
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertBlock(response)
            }
        }
    }
}
