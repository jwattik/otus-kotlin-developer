
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "ok-messenger-be"

//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":ok-messenger-api-log-v1")
include(":ok-messenger-api-v1")
include(":ok-messenger-api-v1-mappers")
include(":ok-messenger-common")
include(":ok-messenger-stubs")
include(":ok-messenger-app")
include(":ok-messenger-biz")
include(":ok-messenger-repo-common")
include(":ok-messenger-repo-inmemory")
include(":ok-messenger-repo-clickhouse")
include(":ok-messenger-repo-stubs")
include(":ok-messenger-repo-tests")
