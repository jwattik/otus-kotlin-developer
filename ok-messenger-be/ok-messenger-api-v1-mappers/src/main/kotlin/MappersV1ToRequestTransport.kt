package ru.otus.messenger.api.v1.mappers

import kotlin.collections.map
import kotlin.collections.toSet
import ru.otus.messenger.api.v1.models.ChatCreateRequestAllOfChat
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMetadata
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat

fun MessengerChat.toTransportCreate() = ChatCreateRequestAllOfChat(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    type = type.takeIf { it != ChatType.NONE }?.toTransportChat(),
    mode = mode.takeIf { it != ChatMode.NONE }?.toTransportChat(),
    ownerId = ownerId.takeIf { it != ChatOwnerId.NONE }?.asString(),
    participants = participants.map { it.asString() }.toSet().takeIf { it.isNotEmpty() },
    metadata = metadata.takeIf { it != ChatMetadata.NONE }?.asString()
)

fun MessengerChat.toTransportRead() = id.takeIf { it != ChatId.NONE }?.asString()

fun MessengerChat.toTransportDelete() = id.takeIf { it != ChatId.NONE }?.asString()

private fun ChatType.toTransportChat() = when (this) {
    ChatType.PRIVATE -> ChatCreateRequestAllOfChat.Type.PRIVATE
    ChatType.GROUP -> ChatCreateRequestAllOfChat.Type.GROUP
    ChatType.CHANNEL -> ChatCreateRequestAllOfChat.Type.CHANNEL
    ChatType.NONE -> null
}

private fun ChatMode.toTransportChat() = when (this) {
    ChatMode.PERSONAL -> ChatCreateRequestAllOfChat.Mode.PERSONAL
    ChatMode.WORK -> ChatCreateRequestAllOfChat.Mode.WORK
    ChatMode.NONE -> null
}