package ru.otus.messenger.common.repo

import ru.otus.messenger.common.exceptions.RepoConcurrencyException
import ru.otus.messenger.common.exceptions.RepoException
import ru.otus.messenger.common.helpers.errorSystem
import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.MessengerChat

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: ChatId) = DbChatResponseErr(
    ChatError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbChatResponseErr(
    ChatError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldChat: MessengerChat,
    exception: Exception = RepoConcurrencyException(
        id = oldChat.id
    ),
) = DbChatResponseErrWithData(
    chat = oldChat,
    err = ChatError(
        code = "${ERROR_GROUP_REPO}-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldChat.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: ChatId) = DbChatResponseErr(
    ChatError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Ad ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbChatResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)