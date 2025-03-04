package ru.otus.messenger.common.exceptions

import ru.otus.messenger.common.models.ChatCommand

class UnknownChatCommand(command: ChatCommand) : Throwable("Wrong command $command at mapping toTransport stage")