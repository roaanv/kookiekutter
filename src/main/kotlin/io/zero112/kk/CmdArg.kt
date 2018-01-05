package io.zero112.kk

import com.beust.jcommander.DynamicParameter
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import io.zero112.kk.util.DefaultArgs

class MainArgs : DefaultArgs() {
//    @Parameter(names = arrayOf("--host"), description = "Host of book list server")
//    var host = "http://unseen:11080"
}

@Parameters(separators = "=", commandDescription = "Create from template")
class CommandGenerate : DefaultArgs() {
    @Parameter(names= ["-t", "--template"],
            description = "The template to generate from. It can point to a file or directory",
            required = true
    )
    var template = "."

    @Parameter(names= ["-v", "--vars"],
            description = "File containing values for variables referenced in the tempalte",
            converter = VarsConverter::class)
    var vars = mutableMapOf<String, Any>()

    @Parameter(names = ["-d", "--dest"],
            description = "Directory to write the files to")
    var dest = "."

    @DynamicParameter(names = ["-D"], description = "Specify or override variables")
    var varsOverride = mutableMapOf<String, String>()
}