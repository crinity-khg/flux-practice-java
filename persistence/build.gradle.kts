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

extra["springCloudVersion"] = "2022.0.4"

dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.cloud:" +
            "spring-cloud-starter-circuitbreaker-reactor-resilience4j")

    // spring cloud stream
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")

    // r2dbc
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    /* test */
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // testcontainer
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")

    // reactor
    testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}