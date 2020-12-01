package com.adazhdw.ktlib.kthttp.callback

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.kthttp.KtConfig
import com.adazhdw.ktlib.kthttp.constant.HttpConstant
import com.adazhdw.ktlib.kthttp.util.ClazzUtil
import com.google.gson.JsonParseException
import java.lang.reflect.Type

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: Gson回调转换泛型类 T
 */
abstract class RequestJsonCallback<T : Any>(owner: LifecycleOwner?) : RequestCallbackImpl(owner) {
    private val mType: Type?

    init {
        mType = getSuperclassTypeParameter(javaClass)
    }

    override fun onResult(result: String) {
        super.onResult(result)
        try {
            val data = KtConfig.converter.convert<T>(result, mType, KtConfig.needDecodeResult)
            KtExecutors.mainThread.execute { this.onSuccess(data) }
        } catch (e: JsonParseException) {
            onFailure(e, HttpConstant.ERROR_JSON_PARSE_EXCEPTION, "Data parse error${e.message}")
        }
    }

    override fun onFailure(e: Exception, code: Int, msg: String?) {
        super.onFailure(e, code, msg)
        this.onError(code, msg)
    }

    abstract fun onSuccess(data: T)
    abstract fun onError(code: Int, msg: String?)

    private fun getSuperclassTypeParameter(subclass: Class<*>): Type {
        return ClazzUtil.getClassType(subclass)
    }

}