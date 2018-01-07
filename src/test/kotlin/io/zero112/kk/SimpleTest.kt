package io.zero112.kk

import io.zero112.kk.util.ArgParser
import io.zero112.kk.util.dirDiff
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import runGenerate
import java.io.File

class SimpleTest {

    private fun runGenerator(genDest: String) {
        val args = arrayOf("generate",
                "-t",
                "./src/test/resources/templates/simple/content",
                "-v",
                "./src/test/resources/templates/simple/myParam.kts",
                "-d", genDest,
                "-Drs=overridden")
        val parser = ArgParser("kookiekutter", MainArgs(), "generate" to CommandGenerate())
        parser.run(args, showHelp = true) {appArgs, cmdArgs ->
            when(cmdArgs) {
                is CommandGenerate -> {
                    cmdArgs.varsOverride.forEach{
                        cmdArgs.vars[it.key] = it.value
                    }
                    runGenerate(appArgs, cmdArgs)
                }
            }
        }
    }

    @Test
    fun basicGen() {
        val genDest = File("/tmp/test-out")
        if (genDest.isDirectory) {
            genDest.deleteRecursively()
        }
        
        runGenerator(genDest.canonicalPath)
        val differences = dirDiff(
                "./src/test/resources/templates/simple/expected",
                "/tmp/test-out",
                ".DS_Store")

        assertTrue(differences.isEmpty(), "Difference in expected generation ${differences.forEach { println(it) }}")
    }
}