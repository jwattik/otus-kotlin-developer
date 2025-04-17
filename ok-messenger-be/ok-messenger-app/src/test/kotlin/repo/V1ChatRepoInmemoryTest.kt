package ru.otus.messenger.app.repo

import ru.otus.messenger.api.v1.models.DebugMode
import ru.otus.messenger.app.common.MessengerAppSettingsData
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.repo.IRepoChat
import ru.otus.messenger.repo.common.ChatRepoInitialized
import ru.otus.messenger.repo.inmemory.ChatRepoInMemory

class V1ChatRepoInmemoryTest : V1ChatRepoBaseTest() {
    override val workMode: DebugMode = DebugMode.TEST
    private fun appSettings(repo: IRepoChat) = MessengerAppSettingsData(
        corSettings = MessengerCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: MessengerAppSettingsData = appSettings(
        repo = ChatRepoInitialized(
            ChatRepoInMemory(randomUuid = { uuidNew })
        )
    )
    override val appSettingsRead: MessengerAppSettingsData = appSettings(
        repo = ChatRepoInitialized(
            ChatRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initChat),
        )
    )
    override val appSettingsDelete: MessengerAppSettingsData = appSettings(
        repo = ChatRepoInitialized(
            ChatRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initChat),
        )
    )
    override val appSettingsSearch: MessengerAppSettingsData = appSettings(
        repo = ChatRepoInitialized(
            ChatRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initChat),
        )
    )
    override val appSettingsResume: MessengerAppSettingsData = appSettings(
        repo = ChatRepoInitialized(
            ChatRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initChat),
        )
    )
}
