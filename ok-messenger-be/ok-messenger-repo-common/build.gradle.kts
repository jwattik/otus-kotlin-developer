plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.coroutines)
    implementation(project(":ok-messenger-common"))

    testImplementation(kotlin("test-junit"))
}