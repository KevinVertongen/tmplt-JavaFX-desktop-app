package utils

import java.io.File

/**
 * Predicate that detects JavaFX jars based on artifact filename.
 */
fun isJavafxJar(file: File): Boolean {
    val name = file.name.lowercase()
    return name.startsWith("javafx") || name.startsWith("openjfx")
}
