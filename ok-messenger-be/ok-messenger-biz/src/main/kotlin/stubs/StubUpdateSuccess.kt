package ru.otus.messenger.biz.stubs

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatId
import ru.otus.messenger.common.models.ChatMode
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker
import ru.otus.messenger.logging.common.LogLevel
import ru.otus.messenger.stubs.MessengerChatStub

fun ICorChainDsl<MessengerContext>.stubUpdateSuccess(title: String, corSettings: MessengerCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для изменения объявления
    """.trimIndent()
    on { stubCase == MessengerStubs.SUCCESS && state == ChatState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubUpdateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = ChatState.FINISHING
            val stub = MessengerChatStub.prepareResult {
                chatRequest.id.takeIf { it != ChatId.NONE }?.also { this.id = it }
                chatRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                chatRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                chatRequest.type.takeIf { it != ChatType.NONE }?.also { this.type = it }
                chatRequest.mode.takeIf { it != ChatMode.NONE }?.also { this.mode = it }
            }
            chatResponse = stub
        }
    }
}
