package ru.otus.messenger.common

import kotlinx.datetime.Instant
import ru.otus.messenger.common.models.*
import ru.otus.messenger.common.stubs.Stubs
import ru.otus.messenger.common.ws.IMessengerWsSession

data class MessengerContext(
    var command: ChatCommand = ChatCommand.NONE,
    var state: ChatState = ChatState.NONE,
    val errors: MutableList<ChatError> = mutableListOf(),

    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,
    var wsSession: IMessengerWsSession = IMessengerWsSession.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var chatRequest: MessengerChat = MessengerChat(),
    var chatFilterRequest: ChatSearchFilter = ChatSearchFilter.NONE,

    var chatResponse: MessengerChat = MessengerChat(),
    var chatsResponse: MutableList<MessengerChat> = mutableListOf(),
)