package ru.otus.messenger.cor.dsl

import ru.otus.messenger.cor.CorDslMarker
import ru.otus.messenger.cor.ICorExec

/**
 * Базовый билдер (dsl)
 */
@CorDslMarker
interface ICorExecDsl<T> {
    var title: String
    var description: String
    fun on(function: suspend T.() -> Boolean)
    fun except(function: suspend T.(e: Throwable) -> Unit)

    fun build(): ICorExec<T>
}
