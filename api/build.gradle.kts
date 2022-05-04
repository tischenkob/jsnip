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

dependencies {
    antlr("org.antlr:antlr4:4.10.1")
    compileOnly("org.jetbrains:annotations:23.0.0")
    implementation("com.squareup:javapoet:1.13.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}