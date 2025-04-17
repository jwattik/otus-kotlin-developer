package ru.otus.messenger.biz.repo

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatResponseErr
import ru.otus.messenger.common.repo.DbChatResponseErrWithData
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == ChatState.RUNNING }
    handle {
        val request = DbChatIdRequest(chatRepoPrepare)
        when(val result = chatRepo.deleteChat(request)) {
            is DbChatResponseOk -> chatRepoDone = result.data
            is DbChatResponseErr -> {
                fail(result.errors)
                chatRepoDone = chatRepoRead
            }
            is DbChatResponseErrWithData -> {
                fail(result.errors)
                chatRepoDone = result.data
            }
        }
    }
}
