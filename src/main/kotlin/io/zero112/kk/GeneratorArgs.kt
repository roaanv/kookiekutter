package io.zero112.kk

import java.io.File

val DEFAULT_VARS_FILE = "._kkVars"

class GeneratorArgs (cmdline: CommandGenerate) {
    val template = cmdline.template
    val dest = cmdline.dest
    val vars = setupVars(cmdline)
    val prompt = cmdline.prompt

    private fun setupVars(cmdLine: CommandGenerate): TemplateVars {
        val vars = TemplateVars()
        val defaultVars = loadDefaultVars(cmdLine)
        vars.putAll(defaultVars)
        vars.putAll(cmdLine.vars)

        cmdLine.varsOverride.forEach{
            vars[it.key] = it.value
        }

        return vars
    }

    private fun loadDefaultVars(cmdLine: CommandGenerate): Map<String, Any> {
        if (cmdLine.excludeDefaultVars) {
            return mapOf()
        }

        val templateSource = File(cmdLine.template)
        if (!templateSource.isDirectory) {
            return mapOf()
        }

        val defaultVarsFile = File(templateSource, DEFAULT_VARS_FILE)
        return if (defaultVarsFile.exists()) {
            VarsConverter().convert(defaultVarsFile.canonicalPath)
        } else {
            mapOf()
        }
    }
}