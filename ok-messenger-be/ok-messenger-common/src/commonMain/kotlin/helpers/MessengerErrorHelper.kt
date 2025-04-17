package ru.otus.messenger.common.helpers

import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.logging.common.LogLevel

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

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = ChatError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = ChatError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)