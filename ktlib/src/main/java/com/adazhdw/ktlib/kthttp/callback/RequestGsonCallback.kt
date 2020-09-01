package com.adazhdw.ktlib.kthttp.callback

import com.adazhdw.ktlib.kthttp.constant.HttpConstant
import com.adazhdw.ktlib.kthttp.util.GsonUtil
import com.google.gson.JsonParseException
import com.google.gson.internal.`$Gson$Types`
import okhttp3.Headers
import okhttp3.Response
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class RequestGsonCallback<T : Any> : RequestCallback {
    private val mType: Type?

    init {
        mType = getSuperclassTypeParameter(javaClass)
    }

    override fun onStart() {

    }

    override fun onResponse(httpResponse: Response?, response: String?, headers: Headers) {

    }

    override fun onSuccess(result: String) {
        try {
            val data = GsonUtil.fromJson<T>(result, mType)
            onSuccess(data)
        } catch (e: JsonParseException) {
            onError(e, HttpConstant.ERROR_JSON_PARSE_EXCEPTION, "data parse error${e.message}")
        }
    }

    abstract fun onSuccess(data: T)
    abstract fun onError(code: Int, msg: String?)

    override fun onError(e: Exception, code: Int, msg: String?) {
        onError(code, msg)
    }

    override fun onFinish() {

    }

    private fun getSuperclassTypeParameter(subclass: Class<*>): Type? {
        val superclass = subclass.genericSuperclass
        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter.")
        }
        val parameterized = superclass as ParameterizedType?
        return if (parameterized != null) {
            `$Gson$Types`.canonicalize(parameterized.actualTypeArguments[0])
        } else null
    }

}