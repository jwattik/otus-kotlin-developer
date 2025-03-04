package ru.otus.messenger.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class ChatArchiveFlag(private val flag: Boolean) {
    fun asString() = flag.toString()

    fun asBoolean() = flag

    companion object {
        val NONE = ChatArchiveFlag(false)
    }
}