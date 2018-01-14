package io.zero112.kk

import io.zero112.kk.util.canonicalFile
import java.io.File
import java.lang.System.getenv


class GeneratorArgs (cmdLine: CommandGenerate) {
    val template = getTemplatePath(cmdLine)
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

    private fun getTemplatePath(cmdLine: CommandGenerate): String {
        var canonicalTemplateFile = canonicalFile(cmdLine.template)
        if (canonicalTemplateFile.exists()) {
            return canonicalTemplateFile.canonicalPath
        }

        val defaultTemplateDir = getenv(DEFAULT_TEMPLATE_DIR_ENV_VAR)
        if (defaultTemplateDir.isNullOrEmpty()) {
            return canonicalTemplateFile.canonicalPath
        }

        return canonicalFile(cmdLine.template, defaultTemplateDir).canonicalPath
    }

    private fun loadDefaultVars(cmdLine: CommandGenerate): Map<String, Any> {
        if (cmdLine.excludeDefaultVars) {
            return mapOf()
        }

        var templateSource = File(getTemplatePath(cmdLine))
        if (templateSource.isFile) {
            templateSource = templateSource.parentFile
        }

        val defaultVarsFile = File(templateSource, DEFAULT_VARS_FILE)
        return if (defaultVarsFile.exists()) {
            VarsConverter().convert(defaultVarsFile.canonicalPath)
        } else {
            mapOf()
        }
    }
}