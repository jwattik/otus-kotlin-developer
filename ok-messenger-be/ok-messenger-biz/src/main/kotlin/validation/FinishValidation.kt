package ru.otus.messenger.biz.validation

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.finishChatValidation(title: String) = worker {
    this.title = title
    on { state == ChatState.RUNNING }
    handle {
        chatValidated = chatValidating
    }
}

fun ICorChainDsl<MessengerContext>.finishChatFilterValidation(title: String) = worker {
    this.title = title
    on { state == ChatState.RUNNING }
    handle {
        chatFilterValidated = chatFilterValidating
    }
}

