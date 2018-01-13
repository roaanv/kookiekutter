package io.zero112.kk.util
import mu.KotlinLogging
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest

data class FileStats(val size: Long, val hash: String)

private val logger = KotlinLogging.logger {}


fun dirDiff(lhsPath: String, rhsPath: String, vararg filesToIgnore: String): List<Pair<String, String>> {
    val lhsFiles = mutableMapOf<String, FileStats>()
    val rhsFiles = mutableMapOf<String, FileStats>()

    val lhsFilePath = File(File(lhsPath).canonicalPath)
    if (!lhsFilePath.isDirectory) {
        throw Exception("lhsPath=$lhsPath is not a valid directory")
    }

    val lhsBase = if (lhsFilePath.absolutePath.endsWith("/")) lhsFilePath.absolutePath else lhsFilePath.absolutePath + "/"

    val rhsFilePath = File(File(rhsPath).canonicalPath)
    if (!rhsFilePath.isDirectory) {
        throw Exception("rhsPath=$rhsPath is not a valid directory")
    }

    val rhsBase = if (rhsFilePath.absolutePath.endsWith("/")) rhsFilePath.absolutePath else rhsFilePath.absolutePath + "/"

    gather(lhsBase, lhsFilePath, lhsFiles, filesToIgnore)
    gather(rhsBase, rhsFilePath, rhsFiles, filesToIgnore)
    return calcDiff(lhsBase, lhsFiles, rhsBase, rhsFiles)
}

private fun calcDiff(lhsBase: String,
                     lhs: MutableMap<String, FileStats>,
                     rhsBase: String,
                     rhs: MutableMap<String, FileStats>): List<Pair<String, String>> {
    val differences = mutableListOf<Pair<String, String>>()
    lhs.forEach {
        val lhsStats = it.value
        if (!rhs.containsKey(it.key)) {
            differences.add(Pair(it.key, "Not in rhs=$rhsBase"))
        } else {
            val rhsStats = rhs[it.key]
            if (lhsStats != rhsStats) {
                differences.add(Pair(it.key, it.key))
            }
            rhs.remove(it.key)
        }
    }

    rhs.forEach { k, _ ->
        differences.add(Pair(k, "Not in lhs=$lhsBase"))
    }

    return differences
}

private fun gather(base: String, source: File, fileList: MutableMap<String, FileStats>, filesToIgnore: Array<out String>) {
    source.list()
            .asSequence()
            .map { File(source, it) }
            .filter { it.name !in filesToIgnore }
            .forEach {
                if (it.isFile) {
                    fileList[it.absolutePath.removePrefix(base)] = FileStats(it.length(), calcHash(it))
                } else {
                    fileList[it.absolutePath.removePrefix(base)] = FileStats(0, "")
                    gather(base, it, fileList, filesToIgnore)
                }
            }
}


fun calcHash(file: File) : String {
    val content = file.readText()
    val HEX_CHARS = "0123456789ABCDEF"
    val digest = MessageDigest.getInstance("SHA-1").digest(content.toByteArray())
    return digest.joinToString(separator = "", transform = {a -> String(charArrayOf(HEX_CHARS[a.toInt() shr 4 and 0x0f], HEX_CHARS[a.toInt() and 0x0f])) })
}

