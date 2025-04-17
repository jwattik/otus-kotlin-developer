package ru.otus.messenger.api.log.v1.mapper

import kotlinx.datetime.Clock
import ru.otus.messenger.api.log.v1.models.*
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.*

fun MessengerContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-messenger",
    chat = toChatLog(),
    errors = errors.map { it.toLog() },
)

private fun MessengerContext.toChatLog(): ChatLogModel? {
    val emptyChat = MessengerChat()
    return ChatLogModel(
        requestId = requestId.takeIf { it != RequestId.NONE }?.asString(),
        requestChat = chatRequest.takeIf { it != emptyChat }?.toLog(),
        requestSearch = chatFilterRequest.takeIf { it != ChatSearchFilter.NONE }?.toLog(),
        responseChat = chatResponse.takeIf { it != emptyChat }?.toLog(),
        responseChats = chatsResponse.takeIf { it.isNotEmpty() }?.filter { it != emptyChat }?.map { it.toLog() },
    ).takeIf { it != ChatLogModel() }
}

private fun ChatSearchFilter.toLog() = ChatSearchLog(
    searchFields = searchFields.joinToString("\t") { it.toString() },
)

private fun ChatError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun MessengerChat.toLog() = ChatLog(
    chatId = id.takeIf { it != ChatId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    type = type.takeIf { it != ChatType.NONE }.toString(),
    mode = mode.takeIf { it != ChatMode.NONE }.toString(),
    ownerId = ownerId.takeIf { it != ChatOwnerId.NONE }?.asString(),
    participants = participants.takeIf { it.isNotEmpty() }?.map { it.asString() }?.toSet(),
    metadata = metadata.takeIf { it != ChatMetadata.NONE }?.asString()
)
