package ru.otus.messenger.repo.clickhouse

import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import ru.otus.messenger.common.NONE
import ru.otus.messenger.common.models.ChatArchiveFlag
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMetadata
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatOwnerId
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.ChatUserId
import ru.otus.messenger.common.models.MessengerChat

class ChatTableRecord(
    val chatId: String?,
    val title: String?,
    val description: String?,
    val type: String?,
    val mode: String?,
    val ownerId: String?,
    val participants: List<String>,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val isArchived: Boolean?,
    val metadata: String?,
) {
    constructor(model: MessengerChat): this(
        chatId = model.id.takeIf { it != ChatId.NONE }?.asString(),
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        type = model.type.takeIf { it != ChatType.NONE }?.name,
        mode = model.mode.takeIf { it != ChatMode.NONE }?.name,
        ownerId = model.ownerId.takeIf { it != ChatOwnerId.NONE }?.asString(),
        participants = model.participants.map { it.asString() },
        createdAt = model.createdAt.takeIf { it != Instant.NONE },
        updatedAt = model.updatedAt.takeIf { it != Instant.NONE },
        isArchived = model.isArchived.takeIf { it != ChatArchiveFlag.NONE }?.asBoolean(),
        metadata = model.metadata.takeIf { it != ChatMetadata.NONE }?.asString()
    )

    fun toInternal() = MessengerChat(
        id = chatId?.let { ChatId(it) } ?: ChatId.NONE,
        title = title ?: "",
        description = description ?: "",
        type = type?.let { ChatType.valueOf(it) } ?: ChatType.NONE,
        mode = mode?.let { ChatMode.valueOf(it) } ?: ChatMode.NONE,
        ownerId = ownerId?.let { ChatOwnerId(it) } ?: ChatOwnerId.NONE,
        participants = participants.map { ChatUserId(it) }.toMutableSet(),
        createdAt = createdAt ?: Instant.NONE,
        updatedAt = updatedAt ?: Instant.NONE,
        isArchived = isArchived?.let { ChatArchiveFlag(it) } ?: ChatArchiveFlag.NONE,
        metadata = metadata?.let { ChatMetadata(Json.parseToJsonElement(it).jsonObject) } ?: ChatMetadata.NONE,
    )
}
