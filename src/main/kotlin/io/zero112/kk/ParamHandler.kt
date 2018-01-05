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
val v = mutableMapOf<String, Any?>()

fun getVars(): MutableMap<String,Any?> {
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

//        val engine = KotlinJsr223JvmLocalScriptEngineFactory().scriptEngine
        val engine = getPreppedEngine()
//        engine.put("p", vars)
//        vars["in"] = -123
        engine.eval(FileReader(filename))
//        val bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE)

        val inv = engine as Invocable
        return inv.invokeFunction("getVars") as MutableMap<String, Any>
    }
}