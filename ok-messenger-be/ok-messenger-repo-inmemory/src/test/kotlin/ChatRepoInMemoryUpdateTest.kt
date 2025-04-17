package ru.otus.messenger.repo.inmemory

import ru.otus.messenger.repo.common.ChatRepoInitialized
import ru.otus.messenger.repo.tests.RepoChatDeleteTest
import ru.otus.messenger.repo.tests.RepoChatUpdateTest

class ChatRepoInMemoryUpdateTest : RepoChatUpdateTest() {
    override val repo = ChatRepoInitialized(
        ChatRepoInMemory(),
        initObjects = RepoChatDeleteTest.Companion.initObjects,
    )
}