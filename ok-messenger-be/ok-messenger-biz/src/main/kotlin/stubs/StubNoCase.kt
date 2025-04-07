package ru.otus.messenger.biz.stubs

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == ChatState.RUNNING }
    handle {
        fail(
            ChatError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
