package ru.otus.messenger.biz.validation

import ru.otus.messenger.biz.MessengerProcessor
import ru.otus.messenger.common.MessengerCorSettings
import ru.otus.messenger.common.models.ChatCommand

abstract class BaseBizValidationTest {
    protected abstract val command: ChatCommand
    private val settings by lazy { MessengerCorSettings() }
    protected val processor by lazy { MessengerProcessor(settings) }
}
