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
}

dependencies {
    ////////////////////////
    //   Implementation   //
    ////////////////////////

    // Spring
    implementation(libs.springboot.actuator)
    implementation(libs.springboot.web)
    implementation(libs.springboot.validator)

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
    testFixturesImplementation(libs.karate)

    // Swagger
    testFixturesImplementation(libs.swagger.validator.core)

    // Instancio
    testFixturesImplementation(libs.instancio)
}

tasks.withType<Test> {
    useJUnitPlatform()
}