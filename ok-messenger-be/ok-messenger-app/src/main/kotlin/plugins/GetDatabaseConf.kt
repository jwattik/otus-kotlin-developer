package ru.otus.messenger.app.plugins

import io.ktor.server.application.*
import ru.otus.messenger.app.configs.ClickHouseConfig
import ru.otus.messenger.app.configs.ConfigPaths
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import ru.otus.messenger.repo.clickhouse.ChatRepoClickHouse
import ru.otus.messenger.repo.clickhouse.DbProperties
import ru.otus.messenger.common.repo.IRepoChat
import ru.otus.messenger.repo.inmemory.ChatRepoInMemory

fun Application.getDatabaseConf(type: DbType): IRepoChat{
    val dbSettingPath = "${ConfigPaths.REPOSITORY}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "db", "clickhouse" -> initClickHouse()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: 'inmemory', 'clickhouse'"
        )
    }
}

enum class DbType(val confName: String) {
    PROD("prod"),
    TEST("test")
}

fun Application.initInMemory(): IRepoChat {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return ChatRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

fun Application.initClickHouse(): IRepoChat {
    val config = ClickHouseConfig(environment.config)
    return ChatRepoClickHouse(
        properties = DbProperties(
            host = config.host,
            port = config.port,
            user = config.user,
            password = config.password,
        )
    )
}
