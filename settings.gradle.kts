rootProject.name = "template"

startParameter.excludedTaskNames += "bootBuildImage"
startParameter.excludedTaskNames += "bootStartScripts"
startParameter.excludedTaskNames += "bootDistTar"
startParameter.excludedTaskNames += "bootDistZip"
startParameter.excludedTaskNames += "distTar"
startParameter.excludedTaskNames += "distZip"

module("application")
module("desktop")

fun module(name: String, directory: String = name, buildFileName: String = "$name.gradle.kts") {
    val moduleDir = File(settingsDir, directory)
    val buildFile = File(moduleDir, buildFileName)

    when {
        !moduleDir.isDirectory -> throw IllegalArgumentException("$moduleDir is not a directory!")
        !buildFile.isFile -> throw IllegalArgumentException("$buildFile is not a file!")
    }

    include(name)
    project(":$name").projectDir = moduleDir
    project(":$name").buildFileName = buildFile.name
}
