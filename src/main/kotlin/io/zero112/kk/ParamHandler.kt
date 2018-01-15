package io.zero112.kk

import mu.KotlinLogging
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
import java.io.File
import java.io.FileReader
import javax.script.ScriptContext
import javax.script.Invocable
import javax.script.ScriptEngine


class ParamHandler {

    private val logger = KotlinLogging.logger {}

    private fun getPreppedEngine(): ScriptEngine {
        val engine = KotlinJsr223JvmLocalScriptEngineFactory().scriptEngine
        val engineSetup = """
import io.zero112.kk.TemplateVars
import io.zero112.kk.replace

val v = TemplateVars(true)

fun prompt(varName: String, default: Any? = null, promptString: String? = null) {
    v.prompt(varName, default=default, promptString=promptString)
}

fun getVars(): TemplateVars {
    return v
}
            """

        engine.eval(engineSetup)

        return engine
    }

    fun getParams(filename: String): MutableMap<String, Any> {
        val vars = mutableMapOf<String ,Any>()

        if (filename.isNullOrBlank()) {
            return vars
        }

        if (!File(filename).isFile) {
            throw Exception("$filename is not a configuration file")
        }

        val engine = getPreppedEngine()
        engine.eval(FileReader(filename))

        val inv = engine as Invocable
        val configuredParams = inv.invokeFunction("getVars") as MutableMap<String, Any>
        return configuredParams
    }
}