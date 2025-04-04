package ru.otus.messenger.common.models

import kotlinx.datetime.Instant
import ru.otus.messenger.common.NONE

data class MessengerChat(
    var id: ChatId = ChatId.NONE,
    var title: String = "",
    var description: String = "",
    var type: ChatType = ChatType.NONE,
    var mode: ChatMode = ChatMode.NONE,
    var ownerId: ChatOwnerId = ChatOwnerId.NONE,
    val participants: MutableSet<ChatUserId> = mutableSetOf(),
    var createdAt: Instant = Instant.NONE,
    var updatedAt: Instant = Instant.NONE,
    var isArchived: ChatArchiveFlag = ChatArchiveFlag.NONE,
    var metadata: ChatMetadata = ChatMetadata.NONE,
) {
    fun isEmpty() = this == NONE

    fun deepCopy(): MessengerChat = copy()

    companion object {
        private val NONE = MessengerChat()
    }
}