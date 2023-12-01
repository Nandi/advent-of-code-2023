package util

import java.nio.file.Paths

fun loadResource(name: String): List<String> {
    return Paths.get(ClassLoader.getSystemResource("$name.txt").toURI()).toFile().readLines()
}