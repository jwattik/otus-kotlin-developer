package ru.otus.messenger.app.common

import kotlinx.datetime.Clock
import ru.otus.messenger.api.log.v1.mapper.toLog
import ru.otus.messenger.common.MessengerContext
import ru.otus.messenger.common.helpers.asMessengerError
import ru.otus.messenger.common.models.ChatCommand
import ru.otus.messenger.common.models.ChatState
import kotlin.reflect.KClass

suspend inline fun <T> MessengerAppSettings.controllerHelper(
    crossinline getRequest: suspend MessengerContext.() -> Unit,
    crossinline toResponse: suspend MessengerContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MessengerContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = ChatState.FAILING
        ctx.errors.add(e.asMessengerError())
        processor.exec(ctx)
        if (ctx.command == ChatCommand.NONE) {
            ctx.command = ChatCommand.READ
        }
        ctx.toResponse()
    }
}
