package ru.otus.messenger.app.plugins

import io.ktor.server.application.*
import ru.otus.messenger.logging.common.LoggerProvider
import ru.otus.messenger.logging.loggerLogback

fun Application.getLoggerProviderConf(): LoggerProvider = LoggerProvider { loggerLogback(it) }