plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":ok-messenger-api-v1"))
    implementation(project(":ok-messenger-common"))

    testImplementation(kotlin("test-junit"))
}