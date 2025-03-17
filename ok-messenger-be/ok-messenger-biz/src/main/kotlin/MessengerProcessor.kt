package ru.otus.messenger.biz

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.stubs.MessengerChatStub

@Suppress("unused", "RedundantSuspendModifier")
class MessengerProcessor(val corSettings: MessengerCorSettings) {
    suspend fun exec(ctx: MessengerContext) {
        ctx.chatResponse = MessengerChatStub.get()
        ctx.chatsResponse = MessengerChatStub.prepareSearchList("New chat", ChatType.GROUP, ChatMode.PERSONAL).toMutableList()
        ctx.state = ChatState.RUNNING
    }
}