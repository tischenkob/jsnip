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

val lombokVersion = "1.18.22"
val junitVersion = "5.8.1"
dependencies {
    antlr("org.antlr:antlr4:4.10.1")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    implementation("org.projectlombok:lombok:$lombokVersion")
    implementation("com.squareup:javapoet:1.13.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-long-messages", "-package", "org.bodyaq.antlr.java")
    outputDirectory = File("src/main/generated/org/bodyaq/antlr/java")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}