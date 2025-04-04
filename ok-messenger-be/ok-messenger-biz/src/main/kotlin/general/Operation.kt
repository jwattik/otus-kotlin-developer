package ru.otus.messenger.biz.general

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.chain

fun ICorChainDsl<MessengerContext>.operation(
    title: String,
    command: ChatCommand,
    block: ICorChainDsl<MessengerContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == ChatState.RUNNING }
}