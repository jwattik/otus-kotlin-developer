package ru.otus.messenger.common.repo

import ru.otus.messenger.common.models.MessengerChat

data class DbChatsResponseOk(
    val data: List<MessengerChat>
): IDbChatsResponse