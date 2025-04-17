package ru.otus.messenger.common.helpers

import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.models.ChatError
import ru.otus.messenger.common.models.ChatState

fun MessengerContext.addError(vararg error: ChatError) = errors.addAll(error)
fun MessengerContext.addErrors(error: Collection<ChatError>) = errors.addAll(error)

fun MessengerContext.fail(error: ChatError) {
    addError(error)
    state = ChatState.FAILING
}

fun MessengerContext.fail(errors: Collection<ChatError>) {
    addErrors(errors)
    state = ChatState.FAILING
}
