package ru.otus.messenger.biz.validation

import kotlin.collections.first
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.errorValidation
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.ChatSearchFilter.StringSearchField
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.chain
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.validateSearchStringLength(title: String) = chain {
    this.title = title
    this.description = """
        Валидация длины строки поиска в поисковых фильтрах. Допустимые значения:
        - null - не выполняем поиск по строке
        - 3-100 - допустимая длина
        - больше 100 - слишком длинная строка
    """.trimIndent()
    on { state == ChatState.RUNNING }
    worker("Обрезка пустых символов") {
        (chatFilterValidating.searchFields.first() as StringSearchField).stringValue =
            (chatFilterValidating.searchFields.first() as StringSearchField).stringValue.trim() }
    worker {
        this.title = "Проверка кейса длины на 0-2 символа"
        this.description = this.title
        on { state == ChatState.RUNNING && (chatFilterValidating.searchFields.first() as StringSearchField).stringValue.length in (1..2) }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooShort",
                    description = "Search string must contain at least 3 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка кейса длины на более 100 символов"
        this.description = this.title
        on { state == ChatState.RUNNING && (chatFilterValidating.searchFields.first() as StringSearchField).stringValue.length > 100 }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooLong",
                    description = "Search string must be no more than 100 symbols long"
                )
            )
        }
    }
}
