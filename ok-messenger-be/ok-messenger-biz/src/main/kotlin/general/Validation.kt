package ru.otus.messenger.biz.general

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.chain

fun ICorChainDsl<MessengerContext>.validation(block: ICorChainDsl<MessengerContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == ChatState.RUNNING }
}
