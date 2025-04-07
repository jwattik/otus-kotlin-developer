package ru.otus.messenger.biz.validation

import org.junit.Test
import ru.otus.messenger.common.models.ChatCommand

class BizValidationReadTest : BaseBizValidationTest() {
    override val command: ChatCommand = ChatCommand.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)
}