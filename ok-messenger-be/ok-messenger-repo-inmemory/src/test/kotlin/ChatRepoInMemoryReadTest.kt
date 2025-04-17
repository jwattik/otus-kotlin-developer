package ru.otus.messenger.repo.inmemory

import ru.otus.messenger.repo.common.ChatRepoInitialized
import ru.otus.messenger.repo.tests.RepoChatReadTest

class ChatRepoInMemoryReadTest : RepoChatReadTest() {
    override val repo = ChatRepoInitialized(
        ChatRepoInMemory(),
        initObjects = initObjects,
    )
}
