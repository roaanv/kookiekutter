package io.zero112.kk

import freemarker.core.InvalidReferenceException
import io.zero112.kk.util.ArgParser
import io.zero112.kk.util.dirDiff
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import runGenerate
import java.io.File
import org.junit.jupiter.api.Assertions



val TEMPLATE_SOURCE = "./src/test/resources/templates/defaultVars/content"
val EXPECTED_DIR = "./src/test/resources/templates/defaultVars/expected"
class DefaultVars {

    private fun runGenerator(args: Array<String>) {
        val parser = ArgParser("kookiekutter", MainArgs(), "generate" to CommandGenerate())
        parser.run(args, showHelp = true) {appArgs, cmdArgs ->
            when(cmdArgs) {
                is CommandGenerate -> {
                    runGenerate(cmdArgs)
                }
            }
        }
    }

    @Test
    fun useDefaultVars() {
        val genDest = File("/tmp/test-out")
        if (genDest.isDirectory) {
            genDest.deleteRecursively()
        }

        val args = arrayOf("generate",
                "-t",
                TEMPLATE_SOURCE,
                "-d", genDest.canonicalPath,
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
        val genDest = File("/tmp/test-out")
        if (genDest.isDirectory) {
            genDest.deleteRecursively()
        }

        val args = arrayOf("generate",
                "-t",
                TEMPLATE_SOURCE,
                "-d", genDest.canonicalPath,
                "-V",
                "-Drs=overridden")

        assertThrows(InvalidReferenceException::class.java) {
            runGenerator(args)
        }
    }
}