package com.adazhdw.ktlib.kthttp.callback

import com.adazhdw.ktlib.kthttp.model.HttpConstant
import com.adazhdw.ktlib.kthttp.model.KtConfig
import com.adazhdw.ktlib.kthttp.util.ClazzUtil
import com.google.gson.JsonParseException
import okhttp3.Response
import java.lang.reflect.Type

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: Gson回调转换泛型类 T
 */
abstract class RequestJsonCallback<T : Any> : RequestCallbackImpl() {
    private val mType: Type?

    init {
        mType = getSuperclassTypeParameter(javaClass)
    }

    override fun onHttpResponse(httpResponse: Response, result: String) {
        try {
            val data = KtConfig.converter.convert<T>(result, mType, KtConfig.needDecodeResult)
            this.onSuccess(data)
        } catch (e: JsonParseException) {
            onFailure(e, HttpConstant.ERROR_JSON_PARSE_EXCEPTION, "Data parse error${e.message}")
        }
    }

    abstract fun onSuccess(data: T)
    abstract fun onError(code: Int, msg: String?)

    override fun onFailure(e: Exception, code: Int, msg: String?) {
        this.onError(code, msg)
    }

    private fun getSuperclassTypeParameter(subclass: Class<*>): Type? {
        return ClazzUtil.getClassType(subclass)
    }

}