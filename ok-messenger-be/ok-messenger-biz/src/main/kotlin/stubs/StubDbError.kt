package ru.otus.messenger.biz.stubs

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == MessengerStubs.DB_ERROR && state == ChatState.RUNNING }
    handle {
        fail(
            ChatError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
