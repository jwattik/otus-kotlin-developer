package ru.otus.messenger.cor.dsl

import ru.otus.messenger.cor.CorDslMarker
import ru.otus.messenger.cor.ICorExec
import ru.otus.messenger.cor.handlers.CorChain

@CorDslMarker
class CorChainDsl<T>(
) : CorExecDsl<T>(), ICorChainDsl<T> {
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf()
    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): ICorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        blockOn = blockOn,
        blockExcept = blockExcept
    )
}
