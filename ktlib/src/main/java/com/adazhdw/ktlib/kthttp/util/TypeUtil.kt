package com.adazhdw.ktlib.kthttp.util

import com.google.gson.internal.`$Gson$Types`
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * author：daguozhu
 * date-time：2020/9/2 10:44
 * description：
 **/
object TypeUtil {
    fun getClassType(subclass: Class<*>): Type? {
        val superclass = subclass.genericSuperclass
        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter.")
        }
        val parameterized = superclass as ParameterizedType?
        return if (parameterized != null) {
            parameterized.actualTypeArguments[0] as ParameterizedType
        } else null
    }

    fun getGsonParameterizedType(subclass: Class<*>): Type? {
        val superClass = subclass.genericSuperclass
        if (superClass is Class<*>) {
            println("superClass=$superClass")
            throw RuntimeException("Missing type parameter.")
        }
        val parameterized: ParameterizedType = superClass as ParameterizedType
        return `$Gson$Types`
            .canonicalize(parameterized.actualTypeArguments[0])
    }
}