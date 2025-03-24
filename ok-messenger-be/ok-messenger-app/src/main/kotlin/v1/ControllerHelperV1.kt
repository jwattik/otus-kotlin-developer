package ru.otus.messenger.app.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.messenger.api.v1.models.IRequest
import ru.otus.messenger.api.v1.models.IResponse
import ru.otus.messenger.app.common.controllerHelper
import ru.otus.messenger.app.common.MessengerAppSettings
import ru.otus.messenger.api.v1.mappers.fromTransport
import ru.otus.messenger.api.v1.mappers.toTransportChat
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: MessengerAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        fromTransport(receive<Q>())
    },
    { respond(toTransportChat()) },
    clazz,
    logId,
)
