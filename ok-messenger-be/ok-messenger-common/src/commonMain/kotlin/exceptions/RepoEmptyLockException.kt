package ru.otus.messenger.common.exceptions

import ru.otus.messenger.common.models.ChatId

class RepoEmptyLockException(id: ChatId): RepoChatException(
    id,
    "Lock is empty in DB"
)