package ru.otus.messenger.biz.stubs

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.stubNotFound(title: String) = worker {
    this.title = title
    this.description = """
        Entity of interest were not found
    """.trimIndent()

    on { stubCase == MessengerStubs.NOT_FOUND && state == ChatState.RUNNING }

    handle {
        fail(
            ChatError(
                group = "internal",
                code = "internal-db",
                message = "Entity not found error"
            )
        )
    }
}
