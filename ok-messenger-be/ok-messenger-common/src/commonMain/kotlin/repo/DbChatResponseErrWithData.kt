package ru.otus.messenger.common.repo

import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.MessengerChat

data class DbChatResponseErrWithData(
    val data: MessengerChat,
    val errors: List<ChatError> = emptyList()
): IDbChatResponse {
    constructor(chat: MessengerChat, err: ChatError): this(chat, listOf(err))
}