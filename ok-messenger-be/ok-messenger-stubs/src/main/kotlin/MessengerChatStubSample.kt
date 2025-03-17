package ru.otus.messenger.stubs

import java.util.UUID
import kotlin.time.Duration.Companion.hours
import kotlinx.datetime.Instant
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import ru.otus.messenger.common.models.*

object MessengerChatStubSample {
    val chatId = UUID.randomUUID().toString()
    val chatOwnerId = UUID.randomUUID().toString()
    val participants = MutableList(3) { ChatUserId(UUID.randomUUID().toString()) }
    private val timestamp = Instant.parse("2025-01-31T01:30:00.000-05:00")

    val CHAT_SAMPLE_1: MessengerChat
        get() = MessengerChat(
            id = ChatId(chatId),
            title = "New chat",
            description = "New chat description",
            type = ChatType.GROUP,
            mode = ChatMode.PERSONAL,
            ownerId = ChatOwnerId(chatOwnerId),
            participants = (participants + ChatUserId(chatOwnerId)).toMutableSet(),
            createdAt = timestamp,
            updatedAt = timestamp.plus(10.hours),
            isArchived = ChatArchiveFlag(false),
            metadata = ChatMetadata(
                buildJsonObject {
                    put("sampleId", "uuid4")
                    put("testParam", "test")
                }
            )
        )

    val CHAT_SAMPLE_2: MessengerChat
        get() = MessengerChat(
            id = ChatId(chatId),
            title = "New chat",
            description = "New chat description",
            type = ChatType.GROUP,
            mode = ChatMode.WORK,
            ownerId = ChatOwnerId(chatOwnerId),
            participants = (participants + ChatUserId(chatOwnerId)).toMutableSet(),
            createdAt = timestamp.plus(100.hours),
            updatedAt = timestamp.plus(101.hours),
            isArchived = ChatArchiveFlag(false),
            metadata = ChatMetadata(
                buildJsonObject {
                    put("sampleId", "uuid5")
                    put("testParam", "test")
                    put("organization", "BlancLabs")
                }
            )
        )
}