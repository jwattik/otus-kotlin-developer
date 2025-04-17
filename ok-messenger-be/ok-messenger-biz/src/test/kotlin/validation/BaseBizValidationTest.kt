package ru.otus.messenger.biz.validation

import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.repo.common.ChatRepoInitialized
import ru.otus.messenger.repo.inmemory.ChatRepoInMemory
import ru.otus.messenger.stubs.MessengerChatStub

abstract class BaseBizValidationTest {
    protected abstract val command: ChatCommand
    private val repo = ChatRepoInitialized(
        repo = ChatRepoInMemory(),
        initObjects = listOf(
            MessengerChatStub.get(),
        ),
    )
    private val settings by lazy { MessengerCorSettings(repoTest = repo) }
    protected val processor by lazy { MessengerProcessor(settings) }
}
