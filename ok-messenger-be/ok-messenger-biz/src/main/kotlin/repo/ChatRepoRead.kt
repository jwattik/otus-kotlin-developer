package ru.otus.messenger.biz.repo

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatResponseErr
import ru.otus.messenger.common.repo.DbChatResponseErrWithData
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Chat reading from DB"
    on { state == ChatState.RUNNING }
    handle {
        val request = DbChatIdRequest(chatValidated)
        when(val result = chatRepo.readChat(request)) {
            is DbChatResponseOk -> chatRepoRead = result.data
            is DbChatResponseErr -> fail(result.errors)
            is DbChatResponseErrWithData -> {
                fail(result.errors)
                chatRepoRead = result.data
            }
        }
    }
}
