package io.zero112.kk
import io.zero112.kk.util.FreemarkerGen
import io.zero112.kk.util.Stack
import io.zero112.kk.util.stackOf
import java.io.File
import java.nio.file.Paths

val FILES_TO_IGNORE = listOf(DEFAULT_VARS_FILE)

class Generator {
    fun generate(args: GeneratorArgs) {
        val startDir = args.dest

        val targetParent = File(Paths.get(startDir).toAbsolutePath().toString())

        val sourceTemplate = File(args.template)

        if (!sourceTemplate.exists()) {
            throw Exception("Template \"${args.template}\" does not exist")
        }

        val targetDirStack = stackOf(targetParent)
        if (sourceTemplate.isFile) {
            generateFile(sourceTemplate, targetDirStack, args.vars)
        } else {
            walkIt(sourceTemplate, targetDirStack, args.vars)
        }
    }

    private fun walkIt(source: File, targetDir: Stack<File>, variables: TemplateVars) {
        source.list()
                .asSequence()
                .map { File(source, it) }
                .forEach {
                    if (it.isFile) {
                        if (it.name !in FILES_TO_IGNORE) {
                            generateFile(it, targetDir, variables)
                        }
                    } else {
                        val newDir = generateDir(it, targetDir, variables)
                        targetDir.push(newDir)
                        walkIt(it, targetDir, variables)
                        targetDir.pop()
                    }
                }
    }

    private fun generateFile(template: File, parentDir: Stack<File>, variables: TemplateVars) {
        val newFile = File(parentDir.peek(), getMappedName(template, variables))

        template.copyTo(newFile, overwrite = true)

        FreemarkerGen.process(newFile, variables)
    }

    private fun generateDir(source: File, targetDir: Stack<File>, variables: TemplateVars): File {
        val newDirName = getMappedName(source, variables)

        val newDir = File(targetDir.peek(), newDirName)
        newDir.mkdirs()

        return newDir
    }

    private fun getMappedName(source: File, variables: TemplateVars): String {
        return FreemarkerGen.process(source.name, variables)
    }
}

