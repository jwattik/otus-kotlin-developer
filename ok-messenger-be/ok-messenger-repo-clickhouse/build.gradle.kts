plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.coroutines)
    implementation(libs.uuid)
    implementation(libs.clickhouse.client)
    implementation(project(":ok-messenger-common"))
    api(project(":ok-messenger-repo-common"))

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":ok-messenger-repo-tests"))
    testImplementation(project(":ok-messenger-stubs"))
    testImplementation(libs.mockk)
}