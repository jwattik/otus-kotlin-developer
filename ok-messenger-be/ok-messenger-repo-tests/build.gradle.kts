plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.datetime)
    api(libs.kotlin.coroutines)
    api(libs.kotlin.coroutines.test)
    implementation(project(":ok-messenger-common"))
    implementation(project(":ok-messenger-repo-common"))
    implementation(kotlin("test-junit"))

    testImplementation(project(":ok-messenger-stubs"))
}