package ru.otus.messenger.common.ws

interface IMessengerWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : IMessengerWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}