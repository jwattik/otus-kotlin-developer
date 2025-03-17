package ru.otus.messenger.app.v1

import io.ktor.server.application.*
import ru.otus.messenger.api.v1.models.*
import ru.otus.messenger.app.common.MessengerAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createChat::class
suspend fun ApplicationCall.createChat(appSettings: MessengerAppSettings) =
    processV1<ChatCreateRequest, ChatCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readChat::class
suspend fun ApplicationCall.readChat(appSettings: MessengerAppSettings) =
    processV1<ChatReadRequest, ChatReadResponse>(appSettings, clRead, "read")

val clDelete: KClass<*> = ApplicationCall::deleteChat::class
suspend fun ApplicationCall.deleteChat(appSettings: MessengerAppSettings) =
    processV1<ChatDeleteRequest, ChatDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchChat::class
suspend fun ApplicationCall.searchChat(appSettings: MessengerAppSettings) =
    processV1<ChatSearchRequest, ChatSearchResponse>(appSettings, clSearch, "search")
