package ru.otus.messenger.biz.stubs

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для описания чата
    """.trimIndent()
    on { stubCase == MessengerStubs.BAD_DESCRIPTION && state == ChatState.RUNNING }
    handle {
        fail(
            ChatError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}
