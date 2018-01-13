package io.zero112.kk.util

import freemarker.cache.StringTemplateLoader
import freemarker.template.Configuration
import freemarker.template.TemplateMethodModelEx
import io.zero112.kk.Prompt
import io.zero112.kk.TemplateVars
import mu.KotlinLogging
import java.io.File
import java.io.StringWriter
import java.net.URI
import java.util.HashMap

class FreeMarkerPromptModel (val model: TemplateVars): TemplateMethodModelEx  {
    override fun exec(arguments: MutableList<Any?>?): Any {
        val key = arguments!![0].toString()

        if (!model.containsKey(key)) {
            model.put(key, Prompt(arguments[1]))
        }

        return model[key]!!
    }
}


object FreemarkerGen {
    private val logger = KotlinLogging.logger {}


    fun process(source: File, dataModel:TemplateVars) {
        val template = {
            val localCfg = Configuration(Configuration.VERSION_2_3_26)
            localCfg.setDirectoryForTemplateLoading(File(source.parent))
            localCfg.getTemplate(source.name)
        }()

        dataModel.put("prompt", FreeMarkerPromptModel(dataModel))

        val processed = StringWriter()
        template.process(dataModel, processed)

        source.writeText(processed.toString())
    }

    fun process(source: String, dataModel:TemplateVars): String {
        val template = {
            val localCfg = Configuration(Configuration.VERSION_2_3_26)
            val templateLoader = StringTemplateLoader()
            templateLoader.putTemplate("default", source)
            localCfg.templateLoader = templateLoader
            localCfg.getTemplate("default")
        }()

        dataModel.put("prompt", FreeMarkerPromptModel(dataModel))
        val processed = StringWriter()
        template.process(dataModel, processed)

        return processed.toString()
    }
}