package io.zero112.kk

import io.zero112.kk.util.ArgParser
import io.zero112.kk.util.dirDiff
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import runGenerate
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import org.junit.contrib.java.lang.system.EnvironmentVariables



class EnvironmentConfigs : GeneratorTests() {
    @Rule
    val environmentVariables = EnvironmentVariables()

    @Test
    fun templateDirFromEnv() {
        environmentVariables.set(DEFAULT_TEMPLATE_DIR_ENV_VAR, "./src/test/resources/")
        val args = arrayOf("generate",
                "-t",
                "templates/simple/content",
                "-v",
                "./src/test/resources/templates/simple/myParam.kts",
                "-d", getDefaultDest(),
                "-Drs=overridden")


        runGenerator(args)
        val differences = dirDiff(
                "./src/test/resources/templates/simple/expected",
                "/tmp/test-out",
                ".DS_Store")

        Assertions.assertTrue(differences.isEmpty(), "Difference in expected generation ${differences.forEach { println(it) }}")
    }

}