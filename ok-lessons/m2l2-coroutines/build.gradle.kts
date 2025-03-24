plugins {
    alias(libs.plugins.jvm)
}

group = "ru.otus"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines)

    // Homework Hard
    implementation(libs.okhttp) // http client
    implementation(libs.jackson.kotlin) // from string to object

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}