package ru.otus.messenger.repo.inmemory

import ru.otus.messenger.repo.common.ChatRepoInitialized
import ru.otus.messenger.repo.tests.RepoChatSearchTest

class ChatRepoInMemorySearchTest : RepoChatSearchTest() {
    override val repo = ChatRepoInitialized(
        ChatRepoInMemory(),
        initObjects = initObjects,
    )
}
