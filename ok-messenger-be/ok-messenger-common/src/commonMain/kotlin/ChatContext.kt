package ru.otus.messenger.common

import kotlinx.datetime.Instant
import ru.otus.messenger.common.models.*
import ru.otus.messenger.common.stubs.Stubs

data class ChatContext(
    var command: ChatCommand = ChatCommand.NONE,
    var state: ChatState = ChatState.NONE,
    val errors: MutableList<ChatError> = mutableListOf(),

    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var chatRequest: MessengerChat = MessengerChat(),
    var chatFilterRequest: ChatSearchFilter = ChatSearchFilter(),

    var chatResponse: MessengerChat = MessengerChat(),
    var chatsResponse: MutableList<MessengerChat> = mutableListOf(),
)