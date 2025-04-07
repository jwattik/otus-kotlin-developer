package ru.otus.messenger.biz.validation

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.errorValidation
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в ChatId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { chatValidating.id != ChatId.NONE && ! chatValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = chatValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
