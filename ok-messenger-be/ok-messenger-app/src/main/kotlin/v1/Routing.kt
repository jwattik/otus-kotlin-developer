package ru.otus.messenger.app.v1

import io.ktor.server.routing.*
import ru.otus.messenger.app.common.MessengerAppSettings

fun Route.v1Chat(appSettings: MessengerAppSettings) {
    route("chat") {
        post("create") {
            call.createChat(appSettings)
        }
        post("read") {
            call.readChat(appSettings)
        }
        post("delete") {
            call.deleteChat(appSettings)
        }
        post("search") {
            call.searchChat(appSettings)
        }
    }
}