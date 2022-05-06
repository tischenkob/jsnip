import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("com.github.johnrengelman.shadow").version("7.1.2")
}

group = "org.bodyaq"
version = "1.0-SNAPSHOT"

application {
    mainModule.set("org.bodyaq.jsnip.cli")
    mainClass.set("org.bodyaq.jsnippet.cli.JSnippetCliLauncher")
}

repositories {
    mavenCentral()
}

java {
    manifest {
        attributes("Main-Class" to "org.bodyaq.jsnippet.cli.JSnippetCliLauncher")
    }
}

tasks.getByName<ShadowJar>("shadowJar") {
    archiveBaseName.set("jsnippet")
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":api"))
    implementation("info.picocli:picocli:4.6.3")
    annotationProcessor("info.picocli:picocli-codegen:4.6.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    implementation("org.projectlombok:lombok:1.18.22")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}
