plugins {
    alias(libs.plugins.jvm)
}

group = "ru.otus"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}