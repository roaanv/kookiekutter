package io.zero112.kk.util

import java.io.File

fun canonicalFile(path: String, vararg searchPath: String): File {
    val canonicalFile = File(
            if (path.startsWith("~/")) {
                path.replaceFirst("~/", System.getProperty("user.home") + "/")
            } else {
                path
            }
    )

    if (searchPath.isEmpty()) {
        return canonicalFile
    }

    return searchPath
            .map { File(it, path) }
            .firstOrNull { it.exists() }
            ?: File(path)
}