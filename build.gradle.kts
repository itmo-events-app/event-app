import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    jacoco
}

group = "org.itmo"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core:9.22.3")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql:42.6.0")
    annotationProcessor("org.projectlombok:lombok")

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:1.19.1")
    testImplementation("org.testcontainers:junit-jupiter:1.19.1")
    testImplementation("org.testcontainers:postgresql:1.19.1")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = true
    }
    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            setExcludes(listOf("org/itmo/eventapp/main/model/**"))
        }
    }))
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory = layout.buildDirectory.dir("reports/jacocoReport")
}