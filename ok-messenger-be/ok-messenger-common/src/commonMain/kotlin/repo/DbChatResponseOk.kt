package ru.otus.messenger.common.repo

import ru.otus.messenger.common.models.MessengerChat

data class DbChatResponseOk(
    val data: MessengerChat
): IDbChatResponse