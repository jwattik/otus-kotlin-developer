package ru.otus.messenger.biz.repo

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == ChatState.RUNNING }
    handle {
        chatRepoPrepare = chatRepoRead.deepCopy().apply {
            this.title = chatValidated.title
            description = chatValidated.description
            type = chatValidated.type
            mode = chatValidated.mode
        }
    }
}