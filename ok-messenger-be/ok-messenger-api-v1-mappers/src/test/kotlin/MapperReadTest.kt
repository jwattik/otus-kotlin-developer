import java.util.UUID
import kotlin.test.assertEquals
import org.junit.Test
import ru.otus.messenger.api.v1.mappers.fromTransport
import ru.otus.messenger.api.v1.mappers.toTransportChat
import ru.otus.messenger.api.v1.models.Chat
import ru.otus.messenger.api.v1.models.ChatReadRequest
import ru.otus.messenger.api.v1.models.ChatReadResponse
import ru.otus.messenger.api.v1.models.Debug
import ru.otus.messenger.api.v1.models.DebugMode
import ru.otus.messenger.api.v1.models.DebugStubs
import ru.otus.messenger.common.ChatContext
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.models.RequestId
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.stubs.Stubs

class MapperReadTest {
    @Test
    fun fromTransport() {
        val req = ChatReadRequest(
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            ),
            chatId = "chat-id",
        )

        val context = ChatContext()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("", context.chatRequest.title)
        assertEquals("chat-id", context.chatRequest.id.asString())
        assertEquals(ChatType.NONE, context.chatRequest.type)
        assertEquals(ChatMode.NONE, context.chatRequest.mode)
    }

    @Test
    fun toTransport() {
        val context = ChatContext(
            requestId = RequestId(UUID.randomUUID().toString()),
            command = ChatCommand.READ,
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

        val req = context.toTransportChat() as ChatReadResponse

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