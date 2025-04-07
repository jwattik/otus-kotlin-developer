package ru.otus.messenger.biz.validation

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.errorValidation
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в заголовке.
        Отказываем в публикации заголовков, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { chatValidating.title.isNotEmpty() && ! chatValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "title",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
