package ru.otus.messenger.biz.stubs

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора объявления
    """.trimIndent()
    on { stubCase == MessengerStubs.BAD_ID && state == ChatState.RUNNING }
    handle {
        fail(
            ChatError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
