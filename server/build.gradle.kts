import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.google.cloud.tools.jib") version "3.3.1"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
    kotlin("plugin.serialization") version "1.8.21"
}

group = "it.polito.WA2.G34"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:1.16.3")
        mavenBom ("org.springframework.security:spring-security-bom:6.0.3")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.hibernate.validator:hibernate-validator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation ("org.testcontainers:junit-jupiter:1.16.3")
    testImplementation("org.testcontainers:postgresql:1.16.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.0.6")

    // Lab5 - Observation
    // using new @Observed on class and enabled @ObservedAspect
    implementation("org.springframework.boot:spring-boot-starter-aop")
    // enabled endpoint and expose metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    // handleing lifecycle of a span
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    // send span and trace data
    // endpoint is default to "http://locahost:9411/api/v2/spans" by actuator
    // we could setting by management.zipkin.tracing.endpoint
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    // send logs by log Appender through URL
    implementation("com.github.loki4j:loki-logback-appender:1.4.0-rc2")

    implementation("org.keycloak:keycloak-admin-client:21.0.1")

    implementation("org.springframework.boot:spring-boot-starter-websocket")



}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    to {
        image = "app"
    }
    container {
        jvmFlags = listOf("-Xms512m", "-Xmx512m")
        ports = listOf("8080")
    }
}