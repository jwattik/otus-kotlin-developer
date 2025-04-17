package ru.otus.messenger.biz.repo

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.repo.DbChatRequest
import ru.otus.messenger.common.repo.DbChatResponseErr
import ru.otus.messenger.common.repo.DbChatResponseErrWithData
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == ChatState.RUNNING }
    handle {
        val request = DbChatRequest(chatRepoPrepare)
        when(val result = chatRepo.updateChat(request)) {
            is DbChatResponseOk -> chatRepoDone = result.data
            is DbChatResponseErr -> fail(result.errors)
            is DbChatResponseErrWithData -> {
                fail(result.errors)
                chatRepoDone = result.data
            }
        }
    }
}