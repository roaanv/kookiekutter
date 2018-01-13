package io.zero112.kk

import java.io.File
import java.lang.System.getenv


class GeneratorArgs (cmdLine: CommandGenerate) {
    val template = getTemplate(cmdLine)
    val dest = cmdLine.dest
    val vars = setupVars(cmdLine)
    val prompt = cmdLine.prompt

    private fun setupVars(cmdLine: CommandGenerate): TemplateVars {
        val vars = TemplateVars(prompt)
        val defaultVars = loadDefaultVars(cmdLine)
        vars.putAll(defaultVars)
        vars.putAll(cmdLine.vars)

        cmdLine.varsOverride.forEach{
            vars[it.key] = it.value
        }

        return vars
    }

    private fun getTemplate(cmdLine: CommandGenerate): String {
        if (File(cmdLine.template).exists()) {
            return cmdLine.template
        }

        val defaultTemplateDir = getenv(DEFAULT_TEMPLATE_DIR_ENV_VAR)
        if (defaultTemplateDir.isNullOrEmpty()) {
            return cmdLine.template
        }

        return File(defaultTemplateDir, cmdLine.template).canonicalPath
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