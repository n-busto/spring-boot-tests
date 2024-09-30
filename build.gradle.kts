plugins {
    java
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

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    ////////////////////////
    //         Test       //
    ////////////////////////

    // Spring
    testImplementation(libs.springboot.test)

    // Junit
    testRuntimeOnly(libs.junit.launcher)

    // Architecture
    testImplementation(libs.archunit)

    // Karate
    testImplementation(libs.karate)

    // Swagger
    testImplementation(libs.swagger.validator.mockmvc)
    testImplementation(libs.swagger.validator.core)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
