package ru.otus.messenger.common.repo

interface IRepoChat {
    
    suspend fun createChat(rq: DbChatRequest): IDbChatResponse
    suspend fun readChat(rq: DbChatIdRequest): IDbChatResponse
    suspend fun updateChat(rq: DbChatRequest): IDbChatResponse
    suspend fun deleteChat(rq: DbChatIdRequest): IDbChatResponse
    suspend fun searchChat(rq: DbChatFilterRequest): IDbChatsResponse
    
    companion object {
        val NONE = object : IRepoChat {
            override suspend fun createChat(rq: DbChatRequest): IDbChatResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readChat(rq: DbChatIdRequest): IDbChatResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateChat(rq: DbChatRequest): IDbChatResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteChat(rq: DbChatIdRequest): IDbChatResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchChat(rq: DbChatFilterRequest): IDbChatsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
    
}
