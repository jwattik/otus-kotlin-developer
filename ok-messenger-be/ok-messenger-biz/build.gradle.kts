plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ok-messenger-common"))
    implementation(project(":ok-messenger-stubs"))

    testImplementation(kotlin("test-junit"))
}