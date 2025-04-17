plugins {
    id("build-jvm")
}

sourceSets {
    main {
        java.srcDir("src/commonMain/kotlin")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    api(libs.kotlin.datetime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.coroutines)
    api("ru.otus.messenger.libs:ok-messenger-lib-logging")

    testImplementation(kotlin("test-junit"))
}