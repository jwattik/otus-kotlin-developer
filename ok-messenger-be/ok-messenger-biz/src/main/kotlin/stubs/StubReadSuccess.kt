package ru.otus.messenger.biz.stubs

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.stubs.MessengerStubs
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker
import ru.otus.messenger.logging.common.LogLevel
import ru.otus.messenger.stubs.MessengerChatStub

fun ICorChainDsl<MessengerContext>.stubReadSuccess(title: String, corSettings: MessengerCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для чтения чата
    """.trimIndent()
    on { stubCase == MessengerStubs.SUCCESS && state == ChatState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubReadSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = ChatState.FINISHING
            val stub = MessengerChatStub.prepareResult {
                chatRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            }
            chatResponse = stub
        }
    }
}
