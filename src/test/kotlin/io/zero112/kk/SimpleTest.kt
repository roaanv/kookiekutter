package io.zero112.kk

import io.zero112.kk.util.ArgParser
import io.zero112.kk.util.dirDiff
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import runGenerate
import java.io.File

class SimpleTest : GeneratorTests() {

    @Test
    fun basicGen() {
        val args = arrayOf("generate",
                "-t",
                "./src/test/resources/templates/simple/content",
                "-v",
                "./src/test/resources/templates/simple/myParam.kts",
                "-d", getDefaultDest(),
                "-Drs=overridden")


        runGenerator(args)
        val differences = dirDiff(
                "./src/test/resources/templates/simple/expected",
                "/tmp/test-out",
                ".DS_Store")

        assertTrue(differences.isEmpty(), "Difference in expected generation ${differences.forEach { println(it) }}")
    }
}