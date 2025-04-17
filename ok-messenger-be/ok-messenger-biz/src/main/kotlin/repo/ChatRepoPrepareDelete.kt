package ru.otus.messenger.biz.repo

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Готовим данные к удалению из БД".trimIndent()
    on { state == ChatState.RUNNING }
    handle {
        chatRepoPrepare = chatValidated.deepCopy()
    }
}
