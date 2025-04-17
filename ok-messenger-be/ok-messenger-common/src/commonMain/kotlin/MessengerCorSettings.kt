package ru.otus.messenger.common

import ru.otus.messenger.common.repo.IRepoChat
import ru.otus.messenger.common.ws.IMessengerWsSessionRepo
import ru.otus.messenger.logging.common.LoggerProvider

data class MessengerCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: IMessengerWsSessionRepo = IMessengerWsSessionRepo.NONE,
    val repoStub: IRepoChat = IRepoChat.NONE,
    val repoTest: IRepoChat = IRepoChat.NONE,
    val repoProd: IRepoChat = IRepoChat.NONE,
) {
    companion object {
        val NONE = MessengerCorSettings()
    }
}