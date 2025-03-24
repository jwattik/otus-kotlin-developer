package ru.otus.messenger.app.base

import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import ru.otus.messenger.api.v1.apiV1ResponseSerialize
import ru.otus.messenger.api.v1.models.IResponse
import ru.otus.messenger.common.ws.IMessengerWsSession

data class KtorWsSessionV1(
    private val session: WebSocketSession
) : IMessengerWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV1ResponseSerialize(obj)))
    }
}
