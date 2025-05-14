plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
    id("com.google.cloud.tools.jib") version "3.4.0"
    id("org.openapi.generator") version "7.7.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"


// === OpenAPI config ===
val openApiSpec = "$rootDir/docs/openapi/openapi.yaml"

openApiGenerate {
    inputSpec.set(openApiSpec)
    generatorName.set("spring")
    outputDir.set("$projectDir/src/main/java")

    apiPackage.set("com.example.rest.generated")
    modelPackage.set("com.example.rest.generated.model")
    invokerPackage.set("com.example.rest.generated")

    globalProperties.set(
        mapOf(
            "apis" to "",
            "models" to "",
            "supportingFiles" to "false"
        )
    )

    configOptions.set(
        mapOf(
            "useJakartaEe" to "true",
            "useSpringBoot3" to "true",
            "interfaceOnly" to "true",
            "skipDefaultInterface" to "true",
            "sourceFolder" to "",
            "additionalModelTypeAnnotations" to "@lombok.Data @lombok.AllArgsConstructor @lombok.Builder"
        )
    )
}



java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.security:spring-security-bom:6.2.3")
        mavenBom("org.keycloak.bom:keycloak-adapter-bom:24.0.3")
    }
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Security
    implementation("org.springframework.security:spring-security-messaging")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.keycloak:keycloak-admin-client:24.0.3")

    // Database
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")

    // JWT (JJWT)
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Swagger / OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("com.h2database:h2")

    // Caching
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    to {
        image = "montagsmaler-backend:latest"
    }
}
