package ru.otus.messenger.common.models

import ru.otus.messenger.logging.common.LogLevel

data class ChatError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)