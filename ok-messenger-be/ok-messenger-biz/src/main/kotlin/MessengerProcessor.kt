package ru.otus.messenger.biz

import ru.otus.messenger.biz.general.initStatus
import ru.otus.messenger.biz.general.operation
import ru.otus.messenger.biz.general.stubs
import ru.otus.messenger.biz.general.validation
import ru.otus.messenger.biz.stubs.stubCreateSuccess
import ru.otus.messenger.biz.stubs.stubReadSuccess
import ru.otus.messenger.biz.stubs.stubUpdateSuccess
import ru.otus.messenger.biz.stubs.stubDbError
import ru.otus.messenger.biz.stubs.stubDeleteSuccess
import ru.otus.messenger.biz.stubs.stubNoCase
import ru.otus.messenger.biz.stubs.stubSearchSuccess
import ru.otus.messenger.biz.stubs.stubValidationBadDescription
import ru.otus.messenger.biz.stubs.stubValidationBadId
import ru.otus.messenger.biz.stubs.stubValidationBadTitle
import ru.otus.messenger.biz.validation.finishChatFilterValidation
import ru.otus.messenger.biz.validation.finishChatValidation
import ru.otus.messenger.biz.validation.validateDescriptionHasContent
import ru.otus.messenger.biz.validation.validateDescriptionNotEmpty
import ru.otus.messenger.biz.validation.validateIdNotEmpty
import ru.otus.messenger.biz.validation.validateIdProperFormat
import ru.otus.messenger.biz.validation.validateSearchStringLength
import ru.otus.messenger.biz.validation.validateTitleHasContent
import ru.otus.messenger.biz.validation.validateTitleNotEmpty
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.cor.dsl.rootChain
import ru.otus.messenger.cor.dsl.worker

class MessengerProcessor(
    private val corSettings: MessengerCorSettings = MessengerCorSettings.NONE
) {
    suspend fun exec(ctx: MessengerContext) = businessChain.exec(
        ctx.also { it.corSettings = corSettings }
    )

    private val businessChain = rootChain<MessengerContext> {
        initStatus("Инициализация статуса")

        operation("Создание чата", ChatCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в chatValidating") { chatValidating = chatRequest.deepCopy() }
                worker("Очистка id") { chatValidating.id = ChatId.NONE }
                worker("Очистка заголовка") { chatValidating.title = chatValidating.title.trim() }
                worker("Очистка описания") { chatValidating.description = chatValidating.description.trim() }
                validateTitleNotEmpty("Проверка, что заголовок не пуст")
                validateTitleHasContent("Проверка символов")
                validateDescriptionNotEmpty("Проверка, что описание не пусто")
                validateDescriptionHasContent("Проверка символов")

                finishChatValidation("Завершение проверок")
            }
        }

        operation("Получить чат", ChatCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в chatValidating") { chatValidating = chatRequest.deepCopy() }
                worker("Очистка id") { chatValidating.id = ChatId(chatValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishChatValidation("Успешное завершение процедуры валидации")
            }
        }

        operation("Изменить чат", ChatCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации заголовка")
                stubValidationBadDescription("Имитация ошибки валидации описания")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в chatValidating") { chatValidating = chatRequest.deepCopy() }
                worker("Очистка id") { chatValidating.id = ChatId(chatValidating.id.asString().trim()) }
                worker("Очистка заголовка") { chatValidating.title = chatValidating.title.trim() }
                worker("Очистка описания") { chatValidating.description = chatValidating.description.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateTitleNotEmpty("Проверка на непустой заголовок")
                validateTitleHasContent("Проверка на наличие содержания в заголовке")
                validateDescriptionNotEmpty("Проверка на непустое описание")
                validateDescriptionHasContent("Проверка на наличие содержания в описании")

                finishChatValidation("Успешное завершение процедуры валидации")
            }
        }

        operation("Удалить чат", ChatCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в chatValidating") {
                    chatValidating = chatRequest.deepCopy()
                }
                worker("Очистка id") { chatValidating.id = ChatId(chatValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                finishChatValidation("Успешное завершение процедуры валидации")
            }
        }

        operation("Поиск чата", ChatCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в chatFilterValidating") { chatFilterValidating = chatFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishChatFilterValidation("Успешное завершение процедуры валидации")
            }
        }
    }.build()
}