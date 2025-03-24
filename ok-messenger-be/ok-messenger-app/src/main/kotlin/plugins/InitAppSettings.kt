package ru.otus.messenger.app.plugins

import io.ktor.server.application.*
import ru.otus.messenger.app.common.MessengerAppSettings
import ru.otus.messenger.app.common.MessengerAppSettingsData
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerCorSettings

fun Application.initAppSettings(): MessengerAppSettings {
    val corSettings = MessengerCorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
    return MessengerAppSettingsData(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = MessengerProcessor(corSettings),
    )
}