package ru.otus.messenger.common.helpers

import ru.otus.messenger.common.models.ChatError

fun Throwable.asMessengerError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = ChatError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)