package ru.otus.messenger.api.v1.mappers

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import ru.otus.messenger.api.v1.mappers.exceptions.UnknownRequestClass
import ru.otus.messenger.api.v1.models.*
import ru.otus.messenger.common.models.*
import ru.otus.messenger.common.ChatContext
import ru.otus.messenger.common.stubs.Stubs

fun ChatContext.fromTransport(request: IRequest) = when (request) {
    is ChatCreateRequest -> fromTransport(request)
    is ChatReadRequest -> fromTransport(request)
    is ChatDeleteRequest -> fromTransport(request)
    is ChatSearchRequest -> fromTransport(request)
    is ChatUpdateRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toChatId() = this?.let { ChatId(it) } ?: ChatId.NONE
private fun String?.toOwnerId() = this?.let { ChatOwnerId(it) } ?: ChatOwnerId.NONE
private fun String?.toChatUserId() = this?.let { ChatUserId(it) } ?: ChatUserId.NONE
private fun String?.toChatMetadata() = this?.let { ChatMetadata(Json.parseToJsonElement(it).jsonObject) } ?: ChatMetadata.NONE
private fun Boolean?.toChatArchiveFlag() = this?.let { ChatArchiveFlag(it) } ?: ChatArchiveFlag.NONE
private fun String?.toParticipants() = this?.let { mutableSetOf(it.toChatUserId()) } ?: mutableSetOf()

private fun Debug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    DebugMode.PROD -> WorkMode.PROD
    DebugMode.TEST -> WorkMode.TEST
    DebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun Debug?.transportToStubCase(): Stubs = when (this?.stub) {
    DebugStubs.SUCCESS -> Stubs.SUCCESS
    DebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    DebugStubs.VALUE_ERROR -> Stubs.VALUE_ERROR
    DebugStubs.MISSING_DATA -> Stubs.MISSING_DATA
    DebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    null -> Stubs.NONE
}

fun ChatContext.fromTransport(request: ChatCreateRequest) {
    command = ChatCommand.CREATE
    chatRequest = request.chat?.toInternal() ?: MessengerChat()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ChatContext.fromTransport(request: ChatReadRequest) {
    command = ChatCommand.READ
    chatRequest = request.chatId.toChatId().toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ChatContext.fromTransport(request: ChatDeleteRequest) {
    command = ChatCommand.DELETE
    chatRequest = request.chatId.toChatId().toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ChatContext.fromTransport(request: ChatUpdateRequest) {
    command = ChatCommand.UPDATE
    chatRequest = request.chat?.toInternal() ?: MessengerChat()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun ChatContext.fromTransport(request: ChatSearchRequest) {
    command = ChatCommand.SEARCH
    chatRequest = request.criteria?.toInternal() ?: MessengerChat()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ChatCreateRequestAllOfChat.toInternal(): MessengerChat = MessengerChat(
    title = this.title ?: "",
    description = this.description ?: "",
    type = this.type.fromTransport(),
    mode = this.mode.fromTransport(),
    ownerId = this.ownerId.toOwnerId(),
    participants = this.participants.fromTransport(),
    metadata = this.metadata.toChatMetadata(),
)

private fun ChatUpdateRequestAllOfChat.toInternal(): MessengerChat = MessengerChat(
    id = this.chatId.toChatId(),
    title = this.title ?: "",
    description = this.description ?: "",
    mode = this.mode.fromTransport(),
    isArchived = this.isArchived.toChatArchiveFlag(),
    metadata = this.metadata.toChatMetadata(),
)

private fun ChatSearchRequestAllOfCriteria.toInternal(): MessengerChat = MessengerChat(
    title = this.title ?: "",
    type = this.type.fromTransport(),
    mode = this.mode.fromTransport(),
    participants = this.participant.toParticipants(),
)

private fun ChatId.toInternal(): MessengerChat = if (this.asString() != "") {
    MessengerChat(id = this)
} else {
    MessengerChat()
}

private fun ChatCreateRequestAllOfChat.Type?.fromTransport() = when (this) {
    ChatCreateRequestAllOfChat.Type.PRIVATE -> ChatType.PRIVATE
    ChatCreateRequestAllOfChat.Type.GROUP -> ChatType.GROUP
    ChatCreateRequestAllOfChat.Type.CHANNEL -> ChatType.CHANNEL
    null -> ChatType.NONE
}

private fun ChatCreateRequestAllOfChat.Mode?.fromTransport() = when (this) {
    ChatCreateRequestAllOfChat.Mode.PERSONAL -> ChatMode.PERSONAL
    ChatCreateRequestAllOfChat.Mode.WORK -> ChatMode.WORK
    null -> ChatMode.NONE
}

private fun ChatUpdateRequestAllOfChat.Mode?.fromTransport() = when (this) {
    ChatUpdateRequestAllOfChat.Mode.PERSONAL -> ChatMode.PERSONAL
    ChatUpdateRequestAllOfChat.Mode.WORK -> ChatMode.WORK
    null -> ChatMode.NONE
}

private fun ChatSearchRequestAllOfCriteria.Type?.fromTransport() = when (this) {
    ChatSearchRequestAllOfCriteria.Type.PRIVATE -> ChatType.PRIVATE
    ChatSearchRequestAllOfCriteria.Type.GROUP -> ChatType.GROUP
    ChatSearchRequestAllOfCriteria.Type.CHANNEL -> ChatType.CHANNEL
    null -> ChatType.NONE
}

private fun ChatSearchRequestAllOfCriteria.Mode?.fromTransport() = when (this) {
    ChatSearchRequestAllOfCriteria.Mode.PERSONAL -> ChatMode.PERSONAL
    ChatSearchRequestAllOfCriteria.Mode.WORK -> ChatMode.WORK
    null -> ChatMode.NONE
}

private fun Set<String>?.fromTransport(): MutableSet<ChatUserId> = if (this != null) {
    this.map { it.toChatUserId() }
        .toMutableSet()
        .takeIf { it.isNotEmpty() } ?: mutableSetOf(ChatUserId(""))
} else {
    mutableSetOf()
}
