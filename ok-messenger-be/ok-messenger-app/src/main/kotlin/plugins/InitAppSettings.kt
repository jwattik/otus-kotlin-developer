package ru.otus.messenger.app.plugins

import io.ktor.server.application.*
import ru.otus.messenger.app.base.KtorWsSessionRepo
import ru.otus.messenger.app.common.MessengerAppSettings
import ru.otus.messenger.app.common.MessengerAppSettingsData
import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.repo.stub.ChatRepoStub

fun Application.initAppSettings(): MessengerAppSettings {
    val corSettings = MessengerCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSessions = KtorWsSessionRepo(),
        repoTest = getDatabaseConf(DbType.TEST),
        repoProd = getDatabaseConf(DbType.PROD),
        repoStub = ChatRepoStub(),
    )
    return MessengerAppSettingsData(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = MessengerProcessor(corSettings),
    )
}