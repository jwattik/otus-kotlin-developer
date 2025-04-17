package ru.otus.messenger.repo.stub

import ru.otus.messenger.common.models.ChatType
import ru.otus.messenger.common.repo.DbChatFilterRequest
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatRequest
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.common.repo.IDbChatResponse
import ru.otus.messenger.common.repo.IDbChatsResponse
import ru.otus.messenger.common.repo.IRepoChat
import ru.otus.messenger.stubs.MessengerChatStub

class ChatRepoStub : IRepoChat {
    override suspend fun createChat(request: DbChatRequest): IDbChatResponse {
        return DbChatResponseOk(
            data = MessengerChatStub.get(),
        )
    }

    override suspend fun readChat(request: DbChatIdRequest): IDbChatResponse {
        return DbChatResponseOk(
            data = MessengerChatStub.get(),
        )
    }

    override suspend fun updateChat(request: DbChatRequest): IDbChatResponse {
        return DbChatResponseOk(
            data = MessengerChatStub.get(),
        )
    }

    override suspend fun deleteChat(request: DbChatIdRequest): IDbChatResponse {
        return DbChatResponseOk(
            data = MessengerChatStub.get(),
        )
    }

    override suspend fun searchChat(request: DbChatFilterRequest): IDbChatsResponse {
        return DbChatsResponseOk(
            data = MessengerChatStub.prepareSearchList(
                chatTitle = "",
                chatType = ChatType.NONE,
            ),
        )
    }
}
