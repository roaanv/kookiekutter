package io.zero112.kk

import io.zero112.kk.util.ArgParser
import org.junit.jupiter.api.Test
import runGenerate

class SimpleTest {

    private fun runGenerator() {
        val args = arrayOf("generate",
                "-t",
                "./src/test/resources/templates/simple/content",
                "-v",
                "./src/test/resources/templates/simple/myParam.kts",
                "-d", "/tmp/test-out",
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
        runGenerator()
    }
}