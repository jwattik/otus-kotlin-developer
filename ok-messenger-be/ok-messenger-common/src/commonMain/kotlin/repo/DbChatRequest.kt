package ru.otus.messenger.common.repo

import ru.otus.messenger.common.models.MessengerChat

data class DbChatRequest(
    val chat: MessengerChat
)
