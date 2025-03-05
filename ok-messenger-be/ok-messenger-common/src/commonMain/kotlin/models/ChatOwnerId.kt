package ru.otus.messenger.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class ChatOwnerId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ChatOwnerId("")
    }
}