package ru.otus.messenger.common.exceptions

import ru.otus.messenger.common.models.ChatId

class RepoConcurrencyException(id: ChatId): RepoChatException(
    id,
    "Expected lock while actual lock in db"
)
