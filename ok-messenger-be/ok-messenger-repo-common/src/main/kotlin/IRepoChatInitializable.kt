package ru.otus.messenger.repo.common

import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.IRepoChat

interface IRepoChatInitializable : IRepoChat {
    fun save(chats: Collection<MessengerChat>) : Collection<MessengerChat>
}