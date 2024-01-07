plugins {
    `java-library`
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }

    register("prepareKotlinBuildScriptModel") {
    }
}

dependencies {
    // javax.inject
    implementation("javax.inject:javax.inject:1")

    // nullable
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    // reactor
    implementation("io.projectreactor:reactor-core:3.5.4")

    /* test */

    // mockito
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.2.0")

    // reactor
    testImplementation("io.projectreactor:reactor-test")

}