package ru.otus.messenger.repo.tests

import ru.otus.messenger.common.models.MessengerChat
import ru.otus.messenger.common.repo.DbChatFilterRequest
import ru.otus.messenger.common.repo.DbChatIdRequest
import ru.otus.messenger.common.repo.DbChatRequest
import ru.otus.messenger.common.repo.DbChatResponseOk
import ru.otus.messenger.common.repo.DbChatsResponseOk
import ru.otus.messenger.common.repo.IDbChatResponse
import ru.otus.messenger.common.repo.IDbChatsResponse
import ru.otus.messenger.common.repo.IRepoChat

class ChatRepositoryMock(
    private val invokeCreateChat: (DbChatRequest) -> IDbChatResponse = { DEFAULT_CHAT_SUCCESS_EMPTY_MOCK },
    private val invokeReadChat: (DbChatIdRequest) -> IDbChatResponse = { DEFAULT_CHAT_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateChat: (DbChatRequest) -> IDbChatResponse = { DEFAULT_CHAT_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteChat: (DbChatIdRequest) -> IDbChatResponse = { DEFAULT_CHAT_SUCCESS_EMPTY_MOCK },
    private val invokeSearchChat: (DbChatFilterRequest) -> IDbChatsResponse = { DEFAULT_CHATS_SUCCESS_EMPTY_MOCK },
): IRepoChat {
    override suspend fun createChat(request: DbChatRequest): IDbChatResponse {
        return invokeCreateChat(request)
    }

    override suspend fun readChat(request: DbChatIdRequest): IDbChatResponse {
        return invokeReadChat(request)
    }

    override suspend fun updateChat(request: DbChatRequest): IDbChatResponse {
        return invokeUpdateChat(request)
    }

    override suspend fun deleteChat(request: DbChatIdRequest): IDbChatResponse {
        return invokeDeleteChat(request)
    }

    override suspend fun searchChat(request: DbChatFilterRequest): IDbChatsResponse {
        return invokeSearchChat(request)
    }

    companion object {
        val DEFAULT_CHAT_SUCCESS_EMPTY_MOCK = DbChatResponseOk(MessengerChat())
        val DEFAULT_CHATS_SUCCESS_EMPTY_MOCK = DbChatsResponseOk(emptyList())
    }
}
