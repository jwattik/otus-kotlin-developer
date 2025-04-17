package ru.otus.messenger.common

import kotlinx.datetime.Instant
import ru.otus.messenger.common.models.*
import ru.otus.messenger.common.repo.IRepoChat
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.common.ws.IMessengerWsSession

data class MessengerContext(
    var command: ChatCommand = ChatCommand.NONE,
    var state: ChatState = ChatState.NONE,
    val errors: MutableList<ChatError> = mutableListOf(),

    var corSettings: MessengerCorSettings = MessengerCorSettings(),
    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: MessengerStubs = MessengerStubs.NONE,
    var wsSession: IMessengerWsSession = IMessengerWsSession.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    // objects from request
    var chatRequest: MessengerChat = MessengerChat(),
    var chatFilterRequest: ChatSearchFilter = ChatSearchFilter.NONE,

    // objects during validation process
    var chatValidating: MessengerChat = MessengerChat(),
    var chatFilterValidating: ChatSearchFilter = ChatSearchFilter.NONE,

    // objects after validation
    var chatValidated: MessengerChat = MessengerChat(),
    var chatFilterValidated: ChatSearchFilter = ChatSearchFilter.NONE,

    // objects during requests to DB
    var chatRepo: IRepoChat = IRepoChat.NONE,
    var chatRepoRead: MessengerChat = MessengerChat(),  // object, read from repo
    var chatRepoPrepare: MessengerChat = MessengerChat(), // prepare to save to DB
    var chatRepoDone: MessengerChat = MessengerChat(),  // result from DB
    var chatsRepoDone: MutableList<MessengerChat> = mutableListOf(),

    // objects to send to client
    var chatResponse: MessengerChat = MessengerChat(),
    var chatsResponse: MutableList<MessengerChat> = mutableListOf(),
)