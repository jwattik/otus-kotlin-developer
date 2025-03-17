package ru.otus.messenger.app.common

import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerCorSettings

data class MessengerAppSettingsData(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MessengerCorSettings = MessengerCorSettings(),
    override val processor: MessengerProcessor = MessengerProcessor(corSettings),
): MessengerAppSettings
