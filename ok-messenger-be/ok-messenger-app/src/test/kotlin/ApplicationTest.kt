package ru.otus.messenger.app

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.assertEquals
import org.junit.Test
import ru.otus.messenger.app.common.MessengerAppSettingsData
import ru.otus.messenger.common.MessengerCorSettings

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module(MessengerAppSettingsData(corSettings = MessengerCorSettings()))
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.Companion.OK, status)
        }
    }

}