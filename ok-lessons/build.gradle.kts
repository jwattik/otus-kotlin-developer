plugins {
    alias(libs.plugins.jvm) apply false
    alias(libs.plugins.multiplatform) apply false
}

group = "ru.otus"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version
}
