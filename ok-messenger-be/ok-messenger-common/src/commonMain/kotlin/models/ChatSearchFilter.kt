package ru.otus.messenger.common.models

data class ChatSearchFilter(
    var searchString: String = "",
    var ownerId: ChatOwnerId = ChatOwnerId.NONE,
    var type: ChatType = ChatType.NONE,
)