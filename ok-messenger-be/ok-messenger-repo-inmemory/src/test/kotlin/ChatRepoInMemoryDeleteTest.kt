package ru.otus.messenger.repo.inmemory

import ru.otus.messenger.repo.common.ChatRepoInitialized
import ru.otus.messenger.repo.tests.RepoChatDeleteTest

class ChatRepoInMemoryDeleteTest : RepoChatDeleteTest() {
    override val repo = ChatRepoInitialized(
        ChatRepoInMemory(),
        initObjects = initObjects,
    )
}