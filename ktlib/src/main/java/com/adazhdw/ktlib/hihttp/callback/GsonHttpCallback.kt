package com.adazhdw.ktlib.hihttp.callback

import com.google.gson.internal.`$Gson$Types`.canonicalize
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by adazhdw on 2019/12/31.
 */
abstract class GsonHttpCallback<T> : OkHttpCallback {

    val mType: Type

    init {
        val typeClass: Type? = javaClass.genericSuperclass
        if (typeClass is Class<*> || typeClass == null) {
            throw RuntimeException("missing type parameter")
        }
        val parameter: ParameterizedType = typeClass as ParameterizedType
        mType = canonicalize(parameter.actualTypeArguments[0])
    }

    abstract fun onSuccess(data: T)

    override fun onError(e: Exception) {

    }
}