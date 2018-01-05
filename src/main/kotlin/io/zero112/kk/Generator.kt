package io.zero112.kk
import io.zero112.kk.util.FreemarkerGen
import io.zero112.kk.util.Stack
import io.zero112.kk.util.stackOf
import java.io.File
import java.nio.file.Paths

class Generator {
    fun generate(appArgs: MainArgs, cmdArgs: CommandGenerate) {
//        val variables = mapOf<String, String>("one" to "1",
//                "foo" to "bar",
//                "bar" to "d1/d2",
//                "f2" to "df1/df2/df2",
//                "rs" to "root some",
//                "foo_f1" to "dir = foo, file = f1",
//                "we" to "root=f2, sub=bar, file=f2.bar")


        val variables = cmdArgs.vars
        val startDir = cmdArgs.dest

        val targetParent = File(Paths.get(startDir).toAbsolutePath().toString())

        val sourceTemplate = File(cmdArgs.template)

        if (!sourceTemplate.exists()) {
            throw Exception("Template \"${cmdArgs.template}\" does not exist")
        }

        val targetDirStack = stackOf(targetParent)
        if (sourceTemplate.isFile) {
            generateFile(sourceTemplate, targetDirStack, variables)
        } else {
            walkIt(sourceTemplate, targetDirStack, variables)
        }
    }

    private fun walkIt(source: File, targetDir: Stack<File>, variables: Map<String, Any>) {
        source.list()
                .asSequence()
                .map { File(source, it) }
                .forEach {
                    if (it.isFile) {
                        generateFile(it, targetDir, variables)
                    } else {
                        val newDir = generateDir(it, targetDir, variables)
                        targetDir.push(newDir)
                        walkIt(it, targetDir, variables)
                        targetDir.pop()
                    }
                }
    }

    private fun generateFile(template: File, parentDir: Stack<File>, variables: Map<String, Any>) {
        val newFile = File(parentDir.peek(), getMappedName(template, variables))

        template.copyTo(newFile, overwrite = true)

        FreemarkerGen.process(newFile, variables)
    }

    private fun generateDir(source: File, targetDir: Stack<File>, variables: Map<String, Any>): File {
        val newDirName = getMappedName(source, variables)

        val newDir = File(targetDir.peek(), newDirName)
        newDir.mkdirs()

        return newDir
    }

    private fun generateFor(template: File, currDir: File, variables: Map<String, String>) {
        if (template.isDirectory) {

        }
    }

    private fun getMappedName(source: File, variables: Map<String, Any>): String {
        var filenameOnly = source.name

        variables.asSequence().forEach {
            val varName = "\${" + it.key + "}"
            filenameOnly = filenameOnly.replace(varName, it.value.toString())
        }

        return filenameOnly
    }
}

