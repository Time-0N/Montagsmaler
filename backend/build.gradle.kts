plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    java
    id("com.google.cloud.tools.jib") version "3.4.0" // For Docker image building
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // Security
    implementation("org.springframework.security:spring-security-messaging:6.1.5")
    implementation("org.keycloak:keycloak-spring-boot-starter:24.0.1")

    // Database
    runtimeOnly("org.postgresql:postgresql:42.7.3")
    implementation("org.liquibase:liquibase-core:4.25.0")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // Email
    implementation("org.springframework.boot:spring-boot-starter-mail:3.2.0")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.0")
    testImplementation("org.springframework.security:spring-security-test:6.1.5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}