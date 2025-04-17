package ru.otus.messenger.biz.repo

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.ChatState
import ru.otus.messenger.common.repo.DbChatFilterRequest
import ru.otus.messenger.common.repo.DbChatsResponseErr
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Search for chats in DB using filters"
    on { state == ChatState.RUNNING }
    handle {
        val request = DbChatFilterRequest(
            ownerId = chatFilterValidated.ownerId,
            chatType = chatFilterValidated.type,
            chatMode = chatFilterValidated.mode,
            searchFields = chatFilterValidated.searchFields,
        )
        when (val result = chatRepo.searchChat(request)) {
            is DbChatsResponseOk -> chatsRepoDone = result.data.toMutableList()
            is DbChatsResponseErr -> fail(result.errors)
        }
    }
}
