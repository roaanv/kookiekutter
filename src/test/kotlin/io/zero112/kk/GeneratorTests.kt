package io.zero112.kk

import io.zero112.kk.util.ArgParser
import runGenerate
import java.io.File

open class GeneratorTests {
    companion object {
        fun getDefaultDest() : String {
            val genDest = File("/tmp/test-out")
            if (genDest.isDirectory) {
                genDest.deleteRecursively()
            }

            return genDest.canonicalPath
        }
    }

    protected fun runGenerator(args: Array<String>) {
        val parser = ArgParser("kookiekutter", MainArgs(), "generate" to CommandGenerate())
        parser.run(args, showHelp = true) { _, cmdArgs ->
            when(cmdArgs) {
                is CommandGenerate -> {
                    runGenerate(cmdArgs)
                }
            }
        }
    }
}