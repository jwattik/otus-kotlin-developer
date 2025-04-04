package ru.otus.messenger.biz.general

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.chain

fun ICorChainDsl<MessengerContext>.stubs(title: String = "Обработка стабов", block: ICorChainDsl<MessengerContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == WorkMode.STUB && state == ChatState.RUNNING }
}
