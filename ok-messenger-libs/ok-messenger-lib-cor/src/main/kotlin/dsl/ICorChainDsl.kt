package ru.otus.messenger.cor.dsl

import ru.otus.messenger.cor.CorDslMarker

/**
 * Билдер (dsl) для цепочек (chain)
 */
@CorDslMarker
interface ICorChainDsl<T> : ICorExecDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}
