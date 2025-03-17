plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":ok-messenger-common"))

    testImplementation(kotlin("test-junit"))
}