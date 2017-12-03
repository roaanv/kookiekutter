package io.zero112.kk.util

import com.fasterxml.jackson.module.kotlin.*

object JsonMapper  {
    val mapper = jacksonObjectMapper()
}

fun Any.toJson (): String {
    return JsonMapper.mapper.writeValueAsString(this)
}