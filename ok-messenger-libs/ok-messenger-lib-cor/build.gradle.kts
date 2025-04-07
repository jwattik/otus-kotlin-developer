plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.coroutines)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.kotlin.coroutines.test)
}