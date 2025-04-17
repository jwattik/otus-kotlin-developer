package ru.otus.messenger.common.repo

import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.MessengerChat

data class DbChatIdRequest(
    val id: ChatId,
) {
    constructor(chat: MessengerChat): this(chat.id)
}
