package ru.otus.messenger.api.v1.mappers

import kotlinx.datetime.Instant
import ru.otus.messenger.api.v1.models.*
import ru.otus.messenger.common.models.*
import ru.otus.messenger.common.ChatContext
import ru.otus.messenger.common.NONE
import ru.otus.messenger.common.exceptions.UnknownChatCommand


fun ChatContext.toTransportChat(): IResponse = when (val cmd = command) {
    ChatCommand.CREATE -> toTransportCreate()
    ChatCommand.READ -> toTransportRead()
    ChatCommand.DELETE -> toTransportDelete()
    ChatCommand.SEARCH -> toTransportSearch()
    ChatCommand.UPDATE -> toTransportUpdate()
    ChatCommand.NONE -> throw UnknownChatCommand(cmd)
}

fun ChatContext.toTransportCreate() = ChatCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    chat = chatResponse.toTransportChat()
)

fun ChatContext.toTransportRead() = ChatReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    chat = chatResponse.toTransportChat()
)

fun ChatContext.toTransportDelete() = ChatDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
)

fun ChatContext.toTransportSearch() = ChatSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    chats = chatsResponse.toTransportChats()
)

fun ChatContext.toTransportUpdate() = ChatUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    chat = chatResponse.toTransportChat()
)

private fun MessengerChat.toTransportChat(): Chat = Chat(
    id = id.takeIf { it != ChatId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != ChatOwnerId.NONE }?.asString(),
    type = type.toTransportChat(),
    mode = mode.toTransportChat(),
    participants = participants.toTransportChat(),
    createdAt = createdAt.takeIf { it != Instant.NONE }?.toString(),
    updatedAt = updatedAt.takeIf { it != Instant.NONE }?.toString(),
    isArchived = isArchived.toTransportChat(),
    metadata = metadata.toTransportChat(),
)

private fun List<MessengerChat>.toTransportChats(): List<Chat>? = this
    .map { it.toTransportChat() }
    .takeIf { it.isNotEmpty() }

private fun ChatType.toTransportChat() = when (this) {
    ChatType.PRIVATE -> Chat.Type.PRIVATE
    ChatType.GROUP -> Chat.Type.GROUP
    ChatType.CHANNEL -> Chat.Type.CHANNEL
    ChatType.NONE -> null
}

private fun ChatMode.toTransportChat() = when (this) {
    ChatMode.PERSONAL -> Chat.Mode.PERSONAL
    ChatMode.WORK -> Chat.Mode.WORK
    ChatMode.NONE -> null
}

private fun ChatArchiveFlag.toTransportChat() = this.asBoolean()

private fun ChatMetadata.toTransportChat() = this.asString()

private fun MutableSet<ChatUserId>.toTransportChat(): Set<String>? = this
    .map { it.asString() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun List<ChatError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportChat() }
    .takeIf { it.isNotEmpty() }

private fun ChatError.toTransportChat() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    fieldName = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun ChatState.toResult(): ResponseResult? = when (this) {
    ChatState.RUNNING -> ResponseResult.SUCCESS
    ChatState.FAILING -> ResponseResult.ERROR
    ChatState.FINISHING -> ResponseResult.SUCCESS
    ChatState.NONE -> null
}