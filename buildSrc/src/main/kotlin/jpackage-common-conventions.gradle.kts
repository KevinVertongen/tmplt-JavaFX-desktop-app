import org.springframework.boot.gradle.tasks.bundling.BootJar
import utils.isJavafxJar

plugins {
    application
    id("org.springframework.boot")
}

// Active Spring profiles of the packaged app //
val activeProfiles = "prod"

// Modules to include in the runtime-image //
val javaModules = listOf("java.compiler", "java.instrument", "java.naming", "java.sql")
val javafxModules = listOf("javafx.controls", "javafx.fxml")

val buildJpackage = layout.buildDirectory.dir("jpackage")
val inputDir = buildJpackage.map { it.dir("input").asFile }
val javafxModsDir = buildJpackage.map { it.dir("javafx-mods").asFile }
val runtimeImageDir = buildJpackage.map { it.dir("runtime-image").asFile }
val jdkHome = providers.environmentVariable("JAVA_HOME")
    .orElse(providers.provider { System.getProperty("java.home") })
    .map { File(it) }
val bootJar = tasks.named<BootJar>("bootJar").flatMap { it.archiveFile }

tasks.named("jar") {
    enabled = false // Application is distributed as a 'Boot jar'
}

tasks.register<Copy>("prepareJpackageInput") {
    group = "distribution"
    description = "Copy app jar into build/jpackage/input"
    inputs.file(bootJar)

    from(bootJar)
    into(inputDir.get())
}

tasks.register<Copy>("prepareJavafxModules") {
    group = "distribution"
    description = "Copy JavaFX jars from runtimeClasspath into build/jpackage/javafx-mods"
    dependsOn(tasks.named("jar"))

    from({
        configurations.getByName("runtimeClasspath").resolvedConfiguration.resolvedArtifacts
            .map { it.file }
            .filter { file -> isJavafxJar(file) }
    })
    into(javafxModsDir.get())
}

tasks.register<Exec>("jlinkCreateRuntime") {
    group = "distribution"
    description = "Run jlink to create a runtime image including JavaFX modules"
    dependsOn("prepareJpackageInput", "prepareJavafxModules")

    doFirst {
        val javaHomeDir = jdkHome.get()
        val jlinkExe = File(javaHomeDir, "bin/jlink").absolutePath
        val modulePath = listOf(javafxModsDir.get().absolutePath, File(javaHomeDir, "jmods").absolutePath)
            .joinToString(File.pathSeparator)
        val addModulesCsv = (javaModules + javafxModules).joinToString(",")
        val output = runtimeImageDir.get().absolutePath

        commandLine = listOf(
            jlinkExe,
            "--module-path", modulePath,
            "--add-modules", addModulesCsv,
            "--output", output,
            "--strip-debug",
            "--compress", "zip-6",
            "--no-header-files",
            "--no-man-pages"
        )
    }
}

tasks.register<Exec>("jpackageCreateInstaller") {
    group = "distribution"
    description = "Run jpackage to create an installer using the runtime image"
    dependsOn("jlinkCreateRuntime")

    doFirst {
        val javaHomeDir = jdkHome.get()
        val jpackageExe = File(javaHomeDir, "bin/jpackage").absolutePath
        val input = inputDir.get().absolutePath
        val runtimeImg = runtimeImageDir.get().absolutePath
        val mainJarName = bootJar.get().asFile.name
        val outDir = buildJpackage.map { it.dir("output").asFile }.get().absolutePath

        // @formatter:off
        commandLine = mutableListOf<String>().apply {
            add(jpackageExe)
            // add("--win-console") // For debugging only!
            addAll(listOf("--type", "app-image")) // change per platform: msi, dmg, pkg, deb, rpm
            addAll(listOf("--name", "Template"))
            addAll(listOf("--app-version", version.toString()))
            addAll(listOf("--input", input))
            addAll(listOf("--main-jar", mainJarName))
            addAll(listOf("--runtime-image", runtimeImg))
            addAll(listOf("--dest", outDir))
            addAll(listOf("--java-options",
                "-Dspring.profiles.active=${activeProfiles} --enable-native-access=javafx.graphics"))
            // add additional flags (icon, vendor, resource-dir) as needed
        }
        // @formatter:on
    }
}
