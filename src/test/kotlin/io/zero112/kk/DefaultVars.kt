package io.zero112.kk

import freemarker.core.InvalidReferenceException
import io.zero112.kk.util.dirDiff
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class DefaultVars : GeneratorTests() {
    val TEMPLATE_SOURCE = "./src/test/resources/templates/defaultVars/content"
    val EXPECTED_DIR = "./src/test/resources/templates/defaultVars/expected"

    @Test
    fun useDefaultVars() {
        val args = arrayOf("generate",
                "-t",
                TEMPLATE_SOURCE,
                "-d", getDefaultDest(),
                "-Drs=overridden")


        runGenerator(args)
        val differences = dirDiff(
                EXPECTED_DIR + "/withDefault",
                "/tmp/test-out",
                ".DS_Store")

        assertTrue(differences.isEmpty(), "Difference in expected generation ${differences.forEach { println(it) }}")
    }

    @Test
    fun dontUseDefaultVars() {
        val args = arrayOf("generate",
                "-t",
                TEMPLATE_SOURCE,
                "-d", getDefaultDest(),
                "-V",
                "-Drs=overridden")

        assertThrows(InvalidReferenceException::class.java) {
            runGenerator(args)
        }
    }
}