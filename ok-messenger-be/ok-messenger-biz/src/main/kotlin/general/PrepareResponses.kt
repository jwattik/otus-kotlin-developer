package ru.otus.messenger.biz.general

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != WorkMode.STUB }
    handle {
        chatResponse = chatRepoDone
        chatsResponse = chatsRepoDone
        state = when (val st = state) {
            ChatState.RUNNING -> ChatState.FINISHING
            else -> st
        }
    }
}
