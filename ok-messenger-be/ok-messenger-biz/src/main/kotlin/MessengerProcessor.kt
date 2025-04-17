package ru.otus.messenger.biz

import ru.otus.messenger.biz.general.initRepo
import ru.otus.messenger.biz.general.initStatus
import ru.otus.messenger.biz.general.operation
import ru.otus.messenger.biz.general.prepareResult
import ru.otus.messenger.biz.general.stubs
import ru.otus.messenger.biz.general.validation
import ru.otus.messenger.biz.repo.checkLock
import ru.otus.messenger.biz.repo.repoCreate
import ru.otus.messenger.biz.repo.repoDelete
import ru.otus.messenger.biz.repo.repoPrepareCreate
import ru.otus.messenger.biz.repo.repoPrepareDelete
import ru.otus.messenger.biz.repo.repoPrepareUpdate
import ru.otus.messenger.biz.repo.repoRead
import ru.otus.messenger.biz.repo.repoSearch
import ru.otus.messenger.biz.repo.repoUpdate
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
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.cor.dsl.chain
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
        initRepo("Инициализация репозитория")

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

            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание чата в БД")
            }
            prepareResult("Подготовка ответа")
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

            chain {
                title = "Логика чтения"
                repoRead("Чтение чата из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == ChatState.RUNNING }
                    handle { chatRepoDone = chatRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
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

            chain {
                title = "Логика сохранения"
                repoRead("Чтение чата из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление чата в БД")
            }
            prepareResult("Подготовка ответа")
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

            chain {
                title = "Логика удаления"
                repoRead("Чтение чата из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление чата из БД")
            }
            prepareResult("Подготовка ответа")
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

            repoSearch("Поиск чата в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}