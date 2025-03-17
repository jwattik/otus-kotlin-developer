plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.datetime)
    implementation(libs.logback.classic)
    implementation(libs.logback.logstash)
    api(libs.logback.appenders)
    api(libs.logger.fluentd)

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.kotlin.coroutines.test)
}