package ru.otus.messenger.common

import ru.otus.messenger.common.ws.IMessengerWsSessionRepo
import ru.otus.messenger.logging.common.LoggerProvider

data class MessengerCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: IMessengerWsSessionRepo = IMessengerWsSessionRepo.NONE,
) {
    companion object {
        val NONE = MessengerCorSettings()
    }
}