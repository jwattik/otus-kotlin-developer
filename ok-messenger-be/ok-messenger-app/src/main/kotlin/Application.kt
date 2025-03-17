package ru.otus.messenger.app

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.cors.routing.*
import ru.otus.messenger.app.common.MessengerAppSettings
import ru.otus.messenger.app.plugins.initAppSettings

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module(
    appSettings: MessengerAppSettings = initAppSettings(),
) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        /* TODO
            Это временное решение, оно опасно.
            В боевом приложении здесь должны быть конкретные настройки
        */
        anyHost()
    }
    configureHTTP()
    configureSerialization()

    configureRouting(appSettings)
}