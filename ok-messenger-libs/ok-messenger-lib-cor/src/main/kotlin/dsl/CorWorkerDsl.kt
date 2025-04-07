package ru.otus.messenger.cor.dsl

import ru.otus.messenger.cor.CorDslMarker
import ru.otus.messenger.cor.ICorExec
import ru.otus.messenger.cor.handlers.CorWorker

@CorDslMarker
class CorWorkerDsl<T> : CorExecDsl<T>(), ICorWorkerDsl<T> {
    private var blockHandle: suspend T.() -> Unit = {}
    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept
    )
}