package ru.otus.messenger.cor.dsl

/**
 * Точка входа в dsl построения цепочек.
 * Элементы исполняются последовательно.
 *
 * Пример:
 * ```
 *  rootChain<SomeContext> {
 *      worker {
 *      }
 *      chain {
 *          worker(...) {
 *          }
 *          worker(...) {
 *          }
 *      }
 *  }
 * ```
 */
fun <T> rootChain(function: ICorChainDsl<T>.() -> Unit): ICorChainDsl<T> = CorChainDsl<T>().apply(function)


/**
 * Создает цепочку, элементы которой исполняются последовательно.
 */
fun <T> ICorChainDsl<T>.chain(function: ICorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

/**
 * Создает рабочего
 */
fun <T> ICorChainDsl<T>.worker(function: ICorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDsl<T>().apply(function))
}

/**
 * Создает рабочего с on и except по умолчанию
 */
fun <T> ICorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(CorWorkerDsl<T>().also {
        it.title = title
        it.description = description
        it.handle(blockHandle)
    })
}
