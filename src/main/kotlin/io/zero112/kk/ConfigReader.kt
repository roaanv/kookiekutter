package io.zero112.kk

import com.beust.jcommander.IStringConverter
import java.io.File

class KookieVars

public class ConfigReader : IStringConverter<KookieVars> {
    override fun convert(value: String?): KookieVars {
        var varFile = File(value)
        if (!varFile.exists()) {
            throw Exception("Parameter file '$value' does not exist")
        }

        if (!varFile.isFile) {
            throw Exception("'$value' is not a file")
        }

        return KookieVars()
    }

}