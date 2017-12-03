package io.zero112.kk

import com.beust.jcommander.IStringConverter
import java.io.File

class VarsConverter : IStringConverter<MutableMap<String, Any>> {
    override fun convert(value: String): MutableMap<String, Any> {
        return ParamHandler().getParams(value)
    }
}