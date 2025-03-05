import java.util.UUID
import kotlin.test.assertEquals
import org.junit.Test
import ru.otus.messenger.api.v1.mappers.fromTransport
import ru.otus.messenger.api.v1.mappers.toTransportChat
import ru.otus.messenger.api.v1.models.Chat
import ru.otus.messenger.api.v1.models.ChatSearchRequest
import ru.otus.messenger.api.v1.models.ChatSearchRequestAllOfCriteria
import ru.otus.messenger.api.v1.models.ChatSearchResponse
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

class MapperSearchTest {
    @Test
    fun fromTransport() {
        val req = ChatSearchRequest(
            debug = Debug(
                mode = DebugMode.STUB,
                stub = DebugStubs.SUCCESS
            ),
            criteria = ChatSearchRequestAllOfCriteria(
                title = "title",
                type = ChatSearchRequestAllOfCriteria.Type.PRIVATE,
                mode = ChatSearchRequestAllOfCriteria.Mode.PERSONAL,
                participant = UUID.randomUUID().toString(),
            )
        )

        val context = ChatContext()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("title", context.chatRequest.title)
        assertEquals(ChatType.PRIVATE, context.chatRequest.type)
        assertEquals(ChatMode.PERSONAL, context.chatRequest.mode)
    }

    @Test
    fun toTransport() {
        val context = ChatContext(
            requestId = RequestId(UUID.randomUUID().toString()),
            command = ChatCommand.SEARCH,
            state = ChatState.RUNNING,
            chatsResponse = mutableListOf(
                MessengerChat(
                    title = "title",
                    description = "description",
                    type = ChatType.PRIVATE,
                    mode = ChatMode.PERSONAL,
                    ownerId = ChatOwnerId(UUID.randomUUID().toString()),
                ),
                MessengerChat(
                    title = "title-1",
                    description = "description",
                    type = ChatType.GROUP,
                    mode = ChatMode.WORK,
                    ownerId = ChatOwnerId(UUID.randomUUID().toString()),
                )
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

        val req = context.toTransportChat() as ChatSearchResponse

        assertEquals("title", req.chats?.get(0)?.title)
        assertEquals("title-1", req.chats?.get(1)?.title)
        assertEquals(Chat.Type.PRIVATE, req.chats?.get(0)?.type)
        assertEquals(Chat.Type.GROUP, req.chats?.get(1)?.type)
        assertEquals(Chat.Mode.PERSONAL, req.chats?.get(0)?.mode)
        assertEquals(Chat.Mode.WORK, req.chats?.get(1)?.mode)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.fieldName)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}