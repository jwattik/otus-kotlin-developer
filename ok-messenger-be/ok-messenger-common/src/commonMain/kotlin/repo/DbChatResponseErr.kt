package ru.otus.messenger.common.repo

import ru.otus.messenger.common.models.ChatError

data class DbChatResponseErr(
    val errors: List<ChatError> = emptyList()
): IDbChatResponse {
    constructor(err: ChatError): this(listOf(err))
}