package ru.otus.messenger.cor.dsl

import ru.otus.messenger.cor.CorDslMarker

/**
 * Билдер (dsl) для рабочих (worker)
 */
@CorDslMarker
interface ICorWorkerDsl<T> : ICorExecDsl<T> {
    fun handle(function: suspend T.() -> Unit)
}
