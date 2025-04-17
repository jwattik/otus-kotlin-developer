package ru.otus.messenger.repo.inmemory

import ru.otus.messenger.repo.common.ChatRepoInitialized
import ru.otus.messenger.repo.tests.RepoChatCreateTest

class ChatRepoInMemoryCreateTest : RepoChatCreateTest() {
    override val repo = ChatRepoInitialized(
        ChatRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}
