package ru.otus.messenger.cor

data class TestContext(
    var status: CorStatus = CorStatus.NONE,
    var some: Int = 0,
    var history: String = "",
) {
    enum class CorStatus {
        NONE,
        RUNNING,
        FAILING,
        ERROR
    }
}
