plugins {
    `java-library`
}

group = "be.mnt.template"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:4.1.0"))

    testImplementation(platform("org.junit:junit-bom:6.0.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.compileJava {
    options.compilerArgs.add("-Xlint:all")
    options.encoding = "UTF-8"
}

tasks.jar {
    archiveFileName = "${rootProject.name}-${project.name}-${project.version}.jar"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
