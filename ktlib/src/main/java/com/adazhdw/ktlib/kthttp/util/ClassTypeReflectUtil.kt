/*
 * Copyright (C) 2015 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adazhdw.ktlib.kthttp.util

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object ClassTypeReflectUtil {
    fun getModelClazz(subclass: Class<*>): Type {
        return getGenericType(0, subclass)
    }

    private fun getGenericType(index: Int, subclass: Class<*>): Type {
        val superclass = subclass.genericSuperclass as? ParameterizedType ?: return Any::class.java
        val params = superclass.actualTypeArguments
        if (index >= params.size || index < 0) {
            throw RuntimeException("Index outof bounds")
        }
        return if (params[index] !is Class<*>) {
            Any::class.java
        } else params[index]
    }
}