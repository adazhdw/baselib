package com.adazhdw.ktlib.kthttp.util

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * author：daguozhu
 * date-time：2020/9/2 10:44
 * description：
 **/
object ClazzUtil {
    fun getClassType(subclass: Class<*>): Type {
        val superclass = subclass.genericSuperclass
        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter.")
        }
        val parameterized: ParameterizedType = superclass as ParameterizedType
        return TypeUtil.canonicalize(parameterized.actualTypeArguments[0])
    }
}