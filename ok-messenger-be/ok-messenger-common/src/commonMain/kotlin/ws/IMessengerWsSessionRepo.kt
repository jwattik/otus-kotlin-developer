package ru.otus.messenger.common.ws

interface IMessengerWsSessionRepo {
    fun add(session: IMessengerWsSession)
    fun clearAll()
    fun remove(session: IMessengerWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IMessengerWsSessionRepo {
            override fun add(session: IMessengerWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IMessengerWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}