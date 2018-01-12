package io.zero112.kk.util

import freemarker.cache.StringTemplateLoader
import freemarker.template.Configuration
import mu.KotlinLogging
import java.io.File
import java.io.StringWriter
import java.net.URI
import java.util.HashMap


object FreemarkerGen {
    private val logger = KotlinLogging.logger {}


    fun process(source: File, dataModel:Any) {
        val template = {
            val localCfg = Configuration(Configuration.VERSION_2_3_26)
            localCfg.setDirectoryForTemplateLoading(File(source.parent))
            localCfg.getTemplate(source.name)
        }()

        val processed = StringWriter()
        template.process(dataModel, processed)

        source.writeText(processed.toString())
    }

    fun process(source: String, dataModel:Any): String {
        val template = {
            val localCfg = Configuration(Configuration.VERSION_2_3_26)
            val templateLoader = StringTemplateLoader()
            templateLoader.putTemplate("default", source)
            localCfg.templateLoader = templateLoader
            localCfg.getTemplate("default")
        }()

        val processed = StringWriter()
        template.process(dataModel, processed)

        return processed.toString()
    }
}