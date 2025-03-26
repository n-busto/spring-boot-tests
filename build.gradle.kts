plugins {
    java
    `java-test-fixtures`
    alias(libs.plugins.springboot.core)
    alias(libs.plugins.springboot.deps)
}

group = "com.nbusto"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    ////////////////////////
    //   Implementation   //
    ////////////////////////

    // Spring
    implementation(libs.springboot.actuator)
    implementation(libs.springboot.web)
    implementation(libs.springboot.validator)
    implementation(libs.spring.boot.docker.compose)

    // Kafka
    implementation(libs.spring.cloud.stream.kafka)
    implementation(libs.spring.cloud.stream.schema)
    implementation(libs.avro.serializer)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testFixturesCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    ////////////////////////
    //         Test       //
    ////////////////////////

    // Spring
    testImplementation(libs.springboot.test)
    testFixturesImplementation(libs.springboot.test)
    testFixturesImplementation(libs.springboot.web)

    // Junit
    testFixturesRuntimeOnly(libs.junit.launcher)
    testFixturesImplementation(libs.junit.api)
    testFixturesImplementation(libs.junit.mockito)

    // Architecture
    testImplementation(libs.archunit)

    // Karate
    testImplementation(libs.karate)

    // Swagger
    testFixturesImplementation(libs.swagger.validator.mockmvc)
    testFixturesImplementation(libs.swagger.validator.core)

    // Instancio
    testFixturesImplementation(libs.instancio)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.1")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}