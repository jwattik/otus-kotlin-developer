package ru.otus.messenger.app.common

import java.util.UUID
import kotlinx.coroutines.test.runTest
import ru.otus.messenger.api.v1.mappers.fromTransport
import ru.otus.messenger.api.v1.mappers.toTransportChat
import ru.otus.messenger.api.v1.models.*
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerTest {
    private val request = ChatCreateRequest(
        chat = ChatCreateRequestAllOfChat(
            title = "New chat title",
            description = "New chat description",
            type = ChatCreateRequestAllOfChat.Type.GROUP,
            mode = ChatCreateRequestAllOfChat.Mode.PERSONAL,
            ownerId = UUID.randomUUID().toString(),
            participants = setOf(),
            metadata = """
                { 
                    "organization": "BlancLabs",
                    "sampleName": "B26",
                    "analyte": "DNA"
                }
            """.trimIndent()
        ),
        debug = Debug(mode = DebugMode.STUB, stub = DebugStubs.SUCCESS)
    )

    private val appSettings: MessengerAppSettings = object : MessengerAppSettings {
        override val corSettings: MessengerCorSettings = MessengerCorSettings()
        override val processor: MessengerProcessor = MessengerProcessor(corSettings)
    }

    class TestApplicationCall(private val request: IRequest) {
        var response: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(response: IResponse) {
            this.response = response
        }
    }

    private suspend fun TestApplicationCall.createReport(appSettings: MessengerAppSettings) {
        val response = appSettings.controllerHelper(
            { fromTransport(receive<ChatCreateRequest>()) },
            { toTransportChat() },
            ControllerTest::class,
            "controller-v1-test"
        )
        respond(response)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createReport(appSettings) }
        val response = testApp.response as ChatCreateResponse
        assertEquals(ResponseResult.SUCCESS, response.result)
    }
}
