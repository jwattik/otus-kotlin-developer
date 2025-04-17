package ru.otus.messenger.common.repo

import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import ru.otus.messenger.common.helpers.errorSystem

abstract class ChatRepoBase : IRepoChat {

    protected suspend fun tryChatMethod(timeout: Duration = 10.seconds, ctx: CoroutineContext = Dispatchers.IO, block: suspend () -> IDbChatResponse) = try {
        withTimeout(timeout) {
            withContext(ctx) {
                block()
            }
        }
    } catch (e: Throwable) {
        DbChatResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryChatsMethod(block: suspend () -> IDbChatsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbChatsResponseErr(errorSystem("methodException", e = e))
    }

}