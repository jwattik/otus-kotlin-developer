package ru.otus.messenger.common.repo

import ru.otus.messenger.common.models.MessengerChat

sealed interface IDbChatResponse: IDbResponse<MessengerChat>
