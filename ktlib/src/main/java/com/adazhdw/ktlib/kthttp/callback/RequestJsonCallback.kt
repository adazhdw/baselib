package com.adazhdw.ktlib.kthttp.callback

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.kthttp.KtConfig
import com.adazhdw.ktlib.kthttp.util.ClazzUtil
import okhttp3.ResponseBody
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

    override fun onResult(body: ResponseBody, result: String) {
        super.onResult(body, result)
        val data = KtConfig.converter.convert<T>(result, mType, KtConfig.needDecodeResult)
        KtExecutors.mainThread.execute {
            this.onSuccess(data)
            this.onFinish()
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