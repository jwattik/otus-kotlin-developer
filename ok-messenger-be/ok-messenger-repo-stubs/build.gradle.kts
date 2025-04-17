plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.coroutines)
    implementation(project(":ok-messenger-common"))
    implementation(project(":ok-messenger-stubs"))

    testImplementation(kotlin("test-junit"))
    testImplementation(project(":ok-messenger-repo-tests"))
}