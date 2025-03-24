package ru.otus.messenger.app

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.otus.messenger.app.common.MessengerAppSettings
import ru.otus.messenger.app.v1.v1Chat
import ru.otus.messenger.app.v1.wsHandlerV1

fun Application.configureRouting(appSettings: MessengerAppSettings) {
    install(AutoHeadResponse)
    install(WebSockets)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("v1") {
            v1Chat(appSettings)

            webSocket("/ws") {
                wsHandlerV1(appSettings)
            }
        }
    }
}
