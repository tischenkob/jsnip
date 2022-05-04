plugins {
    `java-library`
    antlr
}

group = "org.bodyaq"
version = "1.0-SNAPSHOT"

sourceSets {
    main {
        java {
            srcDir("src/main/generated")
        }
    }
    test {
        java {
            srcDir("src/test/java")
        }
    }
}

repositories {
    mavenCentral()
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-long-messages", "-package", "org.bodyaq.antlr.java")
    outputDirectory = File("src/main/generated/org/bodyaq/antlr/java")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val lombokVersion = "1.18.24"
val junitVersion = "5.8.2"

dependencies {
    antlr("org.antlr:antlr4:4.10.1")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("org.projectlombok:lombok:$lombokVersion")
    implementation("com.squareup:javapoet:1.13.0")

    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testImplementation("org.codehaus.janino:janino:3.1.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.projectlombok:lombok:$lombokVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}