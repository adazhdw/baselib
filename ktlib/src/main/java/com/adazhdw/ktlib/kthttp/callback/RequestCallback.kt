package com.adazhdw.ktlib.kthttp.callback

import com.adazhdw.ktlib.kthttp.util.ClassTypeReflectUtil
import okhttp3.Headers
import okhttp3.Response
import java.lang.reflect.Type

class RequestCallback<T> : BaseRequestCallback {

    private val mType: Type = ClassTypeReflectUtil.getModelClazz(javaClass)
    override fun onStart() {

    }

    override fun onResponse(httpResponse: Response, response: String?, headers: Headers) {

    }

    override fun onSuccess(result: String) {

    }

    protected fun onSuccess(data: T) {

    }

    override fun onError(code: Int, msg: String) {

    }

    override fun onFinish() {

    }


}