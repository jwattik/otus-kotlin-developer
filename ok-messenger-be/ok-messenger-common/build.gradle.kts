plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

sourceSets {
    main {
        java.srcDir("src/commonMain/kotlin")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlinx.serialization.json)
    api("ru.otus.messenger.libs:ok-messenger-lib-logging")

    testImplementation(kotlin("test-junit"))
}