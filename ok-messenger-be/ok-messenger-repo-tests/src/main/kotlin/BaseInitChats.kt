package ru.otus.messenger.repo.tests

import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat

abstract class BaseInitChats(private val operation: String): IInitObjects<MessengerChat> {
    fun createInitTestModel(
        suffix: String,
        chatOwnerId: ChatOwnerId = ChatOwnerId("TestOwnerId"),
        chatType: ChatType = ChatType.GROUP,
        chatMode: ChatMode = ChatMode.WORK,
    ) = MessengerChat(
        id = ChatId("chat-repo-$operation-$suffix"),
        ownerId = chatOwnerId,
        type = chatType,
        mode = chatMode,
    )
}