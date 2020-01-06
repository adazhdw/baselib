package com.adazhdw.ktlib.hihttp.callback

import com.google.gson.internal.`$Gson$Types`
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by adazhdw on 2020/1/6.
 */

abstract class GsonHttpCallback<T : Any> : OkHttpCallback {

    val mType: Type

    init {
        val typeClass: Type? = javaClass.genericSuperclass
        if (typeClass is Class<*> || typeClass == null) {
            throw RuntimeException("missing type parameter")
        }
        val parameter: ParameterizedType = typeClass as ParameterizedType
        mType = `$Gson$Types`.canonicalize(parameter.actualTypeArguments[0])
    }

    abstract fun onSuccess(data: T)

    override fun onException(e: Exception) {

    }
}