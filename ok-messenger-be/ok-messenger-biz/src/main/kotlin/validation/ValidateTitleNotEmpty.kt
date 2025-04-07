package ru.otus.messenger.biz.validation

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.errorValidation
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.validateTitleNotEmpty(title: String) = worker {
    this.title = title
    on { chatValidating.title.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}