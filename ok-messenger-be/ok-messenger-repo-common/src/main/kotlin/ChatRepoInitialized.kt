package ru.otus.messenger.repo.common

import ru.otus.messenger.common.models.MessengerChat

class ChatRepoInitialized(
    private val repo: IRepoChatInitializable,
    initObjects: Collection<MessengerChat> = emptyList(),
) : IRepoChatInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<MessengerChat> = save(initObjects).toList()
}
