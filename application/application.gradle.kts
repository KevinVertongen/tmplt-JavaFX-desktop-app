import org.springframework.boot.gradle.tasks.bundling.BootJar
import utils.isJavafxJar

plugins {
    application
    id("java-common-conventions")
    id("javafx-common-conventions")
    id("jpackage-common-conventions")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

application {
    mainClass.set("be.mnt.template.application.SpringApplication")
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED,javafx.graphics")
}

tasks.named<BootJar>("bootJar") {
    setClasspath(
        files(classpath?.filter { file ->
            !isJavafxJar(file)
        })
    )

    archiveBaseName.set(rootProject.name)
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("boot")
}

val projectPropertiesProvider = providers.provider {
    mapOf(
        "project" to project.name,
        "version" to project.version.toString()
    )
}

tasks.named<ProcessResources>("processResources") {
    filesMatching("application.yaml") {
        expand(projectPropertiesProvider.get())
    }
}

dependencies {
    implementation(project(":desktop"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
}
