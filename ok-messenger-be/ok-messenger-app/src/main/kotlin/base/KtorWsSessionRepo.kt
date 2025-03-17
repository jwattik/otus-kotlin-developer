package ru.otus.messenger.app.base

import ru.otus.messenger.common.ws.IMessengerWsSession
import ru.otus.messenger.common.ws.IMessengerWsSessionRepo

class KtorWsSessionRepo(): IMessengerWsSessionRepo {
    private val sessions: MutableSet<IMessengerWsSession> = mutableSetOf()
    override fun add(session: IMessengerWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IMessengerWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}
