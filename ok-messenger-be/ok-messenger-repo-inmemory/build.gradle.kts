plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.datetime)
    implementation(libs.db.cache4k)
    implementation(libs.uuid)
    implementation(project(":ok-messenger-common"))
    api(project(":ok-messenger-repo-common"))

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":ok-messenger-repo-tests"))
}