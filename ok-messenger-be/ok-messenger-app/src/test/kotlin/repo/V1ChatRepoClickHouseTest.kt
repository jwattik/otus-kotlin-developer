package ru.otus.messenger.app.repo

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import ru.otus.messenger.api.v1.models.DebugMode
import ru.otus.messenger.app.common.MessengerAppSettings
import ru.otus.messenger.app.common.MessengerAppSettingsData
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.common.repo.IRepoChat
import ru.otus.messenger.repo.clickhouse.ChatRepoClickHouse
import ru.otus.messenger.repo.common.ChatRepoInitialized
import ru.otus.messenger.stubs.MessengerChatStub

class V1ChatRepoClickHouseTest : V1ChatRepoBaseTest() {
    override val workMode: DebugMode = DebugMode.TEST
    private fun appSettings(repo: IRepoChat) = MessengerAppSettingsData(
        corSettings = MessengerCorSettings(
            repoTest = repo
        )
    )
    private val mockkRepo = mockk<ChatRepoClickHouse> {
        every { save(any()) } returnsArgument(0)
        coEvery { createChat(any()) } returns DbChatResponseOk(
            MessengerChatStub.prepareResult {
                id = ChatId(uuidNew)
            }
        )
        coEvery { readChat(any()) } returns DbChatResponseOk(
            MessengerChatStub.prepareResult {
                id = ChatId(uuidOld)
            }
        )
        coEvery { deleteChat(any()) } returns DbChatResponseOk(
            MessengerChatStub.prepareResult {
                id = ChatId(uuidOld)
            }
        )
        coEvery { searchChat(any()) } returns DbChatsResponseOk(
            listOf(
                MessengerChatStub.prepareResult { id = ChatId(uuidOld) }
            )
        )
    }

    override val appSettingsCreate: MessengerAppSettings = appSettings(
        repo = ChatRepoInitialized(
            mockkRepo
        )
    )
    override val appSettingsRead: MessengerAppSettings = appSettings(
        repo = ChatRepoInitialized(
            mockkRepo,
            initObjects = listOf(initChat),
        )
    )
    override val appSettingsDelete: MessengerAppSettings = appSettings(
        repo = ChatRepoInitialized(
            mockkRepo,
            initObjects = listOf(initChat),
        )
    )
    override val appSettingsSearch: MessengerAppSettings = appSettings(
        repo = ChatRepoInitialized(
            mockkRepo,
            initObjects = listOf(initChat),
        )
    )
    override val appSettingsResume: MessengerAppSettings = appSettings(
        repo = ChatRepoInitialized(
            mockkRepo,
            initObjects = listOf(initChat),
        )
    )
}
