package ru.otus.messenger.common.repo

import ru.otus.messenger.common.models.ChatError

@Suppress("unused")
data class DbChatsResponseErr(
    val errors: List<ChatError> = emptyList()
): IDbChatsResponse {
    constructor(err: ChatError): this(listOf(err))
}