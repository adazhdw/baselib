package com.adazhdw.ktlib.kthttp.util

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
}