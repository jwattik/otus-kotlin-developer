package ru.otus.messenger.common.repo

import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatSearchFilter
import ru.otus.messenger.common.models.ChatType

data class DbChatFilterRequest(
    val searchFields: List<ChatSearchFilter.SearchField> = emptyList(),
    val ownerId: ChatOwnerId = ChatOwnerId.NONE,
    val chatType: ChatType = ChatType.NONE,
    val chatMode: ChatMode = ChatMode.NONE,
)
