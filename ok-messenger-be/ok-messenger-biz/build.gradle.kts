plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":ok-messenger-common"))
    implementation(project(":ok-messenger-stubs"))
    implementation("ru.otus.messenger.libs:ok-messenger-lib-cor")

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.kotlin.coroutines.test)
}