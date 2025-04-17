package ru.otus.messenger.biz.general

import ru.otus.messenger.biz.exception.DbNotConfiguredException
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.errorSystem
import ru.otus.messenger.common.helpers.fail
import ru.otus.messenger.common.models.WorkMode
import ru.otus.messenger.common.repo.IRepoChat
import ru.otus.messenger.cor.dsl.ICorChainDsl
import ru.otus.messenger.cor.dsl.worker

fun ICorChainDsl<MessengerContext>.initRepo(title: String) = worker {
    this.title = title
    description = "Estimate main working repo depending on work mode".trimIndent()
    handle {
        chatRepo = when (workMode) {
            WorkMode.TEST -> corSettings.repoTest
            WorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != WorkMode.STUB && chatRepo == IRepoChat.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = DbNotConfiguredException(workMode)
            )
        )
    }
}
