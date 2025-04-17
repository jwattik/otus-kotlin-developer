package ru.otus.messenger.repo.clickhouse

data class DbProperties(
    val host: String = "localhost",
    val port: Int = 8443,
    val user: String = "default",
    val password: String = "",
)
