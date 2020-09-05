package com.adazhdw.ktlib.kthttp.callback

import com.adazhdw.ktlib.kthttp.model.HttpConstant
import com.adazhdw.ktlib.kthttp.util.GsonUtils
import com.adazhdw.ktlib.kthttp.util.TypeUtil
import com.google.gson.JsonParseException
import java.lang.reflect.Type

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: Gson回调转换泛型类 T
 */
abstract class RequestGsonCallback<T : Any> : RequestCallbackImpl() {
    private val mType: Type?

    init {
        mType = getSuperclassTypeParameter(javaClass)
    }

    override fun onResponse(result: String) {
        try {
            val data = GsonUtils.fromJson<T>(result, mType)
            onSuccess(data)
        } catch (e: JsonParseException) {
            onFailure(e, HttpConstant.ERROR_JSON_PARSE_EXCEPTION, "data parse error${e.message}")
        }
    }

    abstract fun onSuccess(data: T)
    abstract fun onError(code: Int, msg: String?)

    override fun onFailure(e: Exception, code: Int, msg: String?) {
        onError(code, msg)
    }

    private fun getSuperclassTypeParameter(subclass: Class<*>): Type? {
        return TypeUtil.getClassType(subclass)
    }

}