plugins {
    id("build-jvm")
    alias(libs.plugins.openapi.generator)
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/kotlin"))
    }
}

openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.log.v1"
    generatorName.set("kotlin")
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    inputSpec.set(rootProject.ext["spec-log-v1"] as String)

    /**
     * Use only models
     * Doc: https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Additional parameters from
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )
    )
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.datatype)
    implementation(project(":ok-messenger-common"))

    testImplementation(kotlin("test-junit"))
}

tasks {
    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerate)
    }
}