import java.util.UUID
import kotlin.test.assertEquals
import org.junit.Test
import ru.otus.messenger.api.v1.mappers.fromTransport
import ru.otus.messenger.api.v1.mappers.toTransportChat
import ru.otus.messenger.api.v1.models.Chat
import ru.otus.messenger.api.v1.models.ChatUpdateRequest
import ru.otus.messenger.api.v1.models.ChatUpdateRequestAllOfChat
import ru.otus.messenger.api.v1.models.ChatUpdateResponse
import ru.otus.messenger.api.v1.models.Debug
import ru.otus.messenger.api.v1.models.DebugMode
import ru.otus.messenger.api.v1.models.DebugStubs
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.models.RequestId
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.stubs.MessengerStubs

class MapperUpdateTest {
    @Test
    fun fromTransport() {
        val req = ChatUpdateRequest(
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            ),
            chat = ChatUpdateRequestAllOfChat(
                chatId = "chat-id",
                title = "title",
                description = "description",
                mode = ChatUpdateRequestAllOfChat.Mode.PERSONAL,
                isArchived = false,
                metadata = """
                    {
                        "testParam": "test"
                    }
                """.trimIndent()
            )
        )

        val context = MessengerContext()
        context.fromTransport(req)

        assertEquals(MessengerStubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("chat-id", context.chatRequest.id.asString())
        assertEquals("title", context.chatRequest.title)
        assertEquals(ChatType.NONE, context.chatRequest.type)
        assertEquals(ChatMode.PERSONAL, context.chatRequest.mode)
    }

    @Test
    fun toTransport() {
        val context = MessengerContext(
            requestId = RequestId(UUID.randomUUID().toString()),
            command = ChatCommand.UPDATE,
            state = ChatState.RUNNING,
            chatResponse = MessengerChat(
                title = "title",
                description = "description",
                type = ChatType.PRIVATE,
                mode = ChatMode.PERSONAL,
                ownerId = ChatOwnerId(UUID.randomUUID().toString()),
            ),
            errors = mutableListOf(
                ChatError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            )
        )

        val req = context.toTransportChat() as ChatUpdateResponse

        assertEquals("title", req.chat?.title)
        assertEquals(Chat.Type.PRIVATE, req.chat?.type)
        assertEquals(Chat.Mode.PERSONAL, req.chat?.mode)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.fieldName)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}