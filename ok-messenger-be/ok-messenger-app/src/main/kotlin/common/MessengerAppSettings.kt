package ru.otus.messenger.app.common

import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerCorSettings

interface MessengerAppSettings {
    val processor: MessengerProcessor
    val corSettings: MessengerCorSettings
}