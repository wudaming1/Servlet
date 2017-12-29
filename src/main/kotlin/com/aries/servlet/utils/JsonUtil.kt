package com.aries.servlet.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object JsonUtil{


    private val mapper = jacksonObjectMapper()

    fun <T> readValue(jsonString: String, clazz: Class<T>): T? {
        var result: T? = null
        try {

            result = mapper.readValue<T>(jsonString, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    fun writeValueAsString(any: Any): String = mapper.writeValueAsString(any)
}