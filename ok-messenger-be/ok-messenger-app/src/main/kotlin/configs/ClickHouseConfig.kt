package ru.otus.messenger.app.configs

import io.ktor.server.config.ApplicationConfig

data class ClickHouseConfig(
    val host: String = "localhost",
    val port: Int = 8443,
    val user: String = "default",
    val password: String = "",
) {
    constructor(config: ApplicationConfig): this(
        host = config.propertyOrNull("$PATH.host")?.getString() ?: "localhost",
        port = config.propertyOrNull("$PATH.port")?.getString()?.toIntOrNull() ?: 8443,
        user = config.propertyOrNull("$PATH.user")?.getString() ?: "default",
        password = config.property("$PATH.password").getString(),
    )

    companion object {
        const val PATH = "${ConfigPaths.REPOSITORY}.db"
    }
}
