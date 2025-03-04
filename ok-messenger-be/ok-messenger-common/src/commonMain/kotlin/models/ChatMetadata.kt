package ru.otus.messenger.common.models

import kotlin.jvm.JvmInline
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

@JvmInline
value class ChatMetadata(private val metadata: JsonObject) {
    fun asString() = metadata.toString()

    companion object {
        val NONE = ChatMetadata(buildJsonObject {})
    }
}