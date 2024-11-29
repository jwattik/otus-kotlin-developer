plugins {
    kotlin("jvm")
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