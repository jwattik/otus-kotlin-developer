package ru.otus.messenger.common.exceptions

import ru.otus.messenger.common.models.ChatId

open class RepoChatException(
    @Suppress("unused")
    val chatId: ChatId,
    msg: String,
): RepoException(msg)