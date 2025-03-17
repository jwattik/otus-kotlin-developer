dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "otus-kotlin-developer"

includeBuild("build-plugin")

includeBuild("ok-lessons")

includeBuild("ok-messenger-be")

includeBuild("ok-messenger-libs")
