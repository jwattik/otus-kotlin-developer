package ru.otus.messenger.stubs

import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.stubs.MessengerChatStubSample.CHAT_SAMPLE_1
import ru.otus.messenger.stubs.MessengerChatStubSample.CHAT_SAMPLE_2

object MessengerChatStub {
    fun get(): MessengerChat = CHAT_SAMPLE_1.copy()

    fun prepareResult(block: MessengerChat.() -> Unit): MessengerChat = get().apply(block)

    fun prepareSearchList(
        chatTitle: String,
        chatType: ChatType,
    ) = listOf(
        CHAT_SAMPLE_1,
        CHAT_SAMPLE_2
    ).filter { it.title == chatTitle && it.type == chatType }
}