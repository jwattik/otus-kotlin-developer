package ru.otus.messenger.app.repo

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.stubs.MessengerChatStub
import ru.otus.messenger.app.module
import ru.otus.messenger.app.common.MessengerAppSettings
import ru.otus.messenger.api.v1.models.ChatCreateResponse
import ru.otus.messenger.api.v1.models.DebugMode
import ru.otus.messenger.api.v1.mappers.toTransportCreate
import ru.otus.messenger.api.v1.mappers.toTransportDelete
import ru.otus.messenger.api.v1.mappers.toTransportRead
import ru.otus.messenger.api.v1.models.*

abstract class V1ChatRepoBaseTest {
    abstract val workMode: DebugMode
    abstract val appSettingsCreate: MessengerAppSettings
    abstract val appSettingsRead:   MessengerAppSettings
    abstract val appSettingsDelete: MessengerAppSettings
    abstract val appSettingsSearch: MessengerAppSettings
    abstract val appSettingsResume: MessengerAppSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"
    protected val initChat = MessengerChatStub.prepareResult {
        id = ChatId(uuidOld)
    }

    @Test
    fun create() {
        val chat = initChat.toTransportCreate()
        v1TestApplication(
            settings = appSettingsCreate,
            endpoint = "create",
            request = ChatCreateRequest(
                chat = chat,
                debug = Debug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<ChatCreateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.chat?.id)
            assertEquals(chat.ownerId, responseObj.chat?.ownerId)
            assertEquals(chat.title, responseObj.chat?.title)
            assertEquals(chat.description, responseObj.chat?.description)
        }
    }

    @Test
    fun read() {
        val chat = initChat.toTransportRead()
        v1TestApplication(
            settings = appSettingsRead,
            endpoint = "read",
            request = ChatReadRequest(
                chatId = chat,
                debug = Debug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<IResponse>() as ChatReadResponse
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.chat?.id)
        }
    }

    @Test
    fun delete() {
        val chat = initChat.toTransportDelete()
        v1TestApplication(
            settings = appSettingsDelete,
            endpoint = "delete",
            request = ChatDeleteRequest(
                chatId = chat,
                debug = Debug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<ChatDeleteResponse>()
            assertEquals(200, response.status.value)
            assertEquals(ResponseResult.SUCCESS, responseObj.result)
        }
    }

    @Test
    fun search() = v1TestApplication(
        settings = appSettingsSearch,
        endpoint = "search",
        request = ChatSearchRequest(
            criteria = ChatSearchRequestAllOfCriteria(),
            debug = Debug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<ChatSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.chats?.size)
        assertEquals(uuidOld, responseObj.chats?.first()?.id)
    }

    private inline fun <reified T: IRequest> v1TestApplication(
        settings: MessengerAppSettings,
        endpoint: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(appSettings = settings) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }
        val response = client.post("/v1/chat/$endpoint") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        function(response)
    }
}
