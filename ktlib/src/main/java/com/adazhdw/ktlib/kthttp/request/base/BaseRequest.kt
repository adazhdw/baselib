package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.kthttp.callback.OkHttpCallbackImpl
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.model.KtConfig
import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.ktlib.kthttp.request.RequestCallProxy
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 10:11
 * description：
 **/
abstract class BaseRequest(val url: String, val param: Param) {
    private val okHttpClient: OkHttpClient = KtConfig.mOkHttpClient
    private var mCallProxy: RequestCallProxy? = null
    protected var tag = ""

    abstract fun getRequestBody(): RequestBody

    abstract fun getRequest(requestBody: RequestBody): Request

    /**
     * 执行网络请求
     */
    @Suppress("UNCHECKED_CAST")
    fun execute(callback: RequestCallback?) {
        mCallProxy = RequestCallProxy(getRawCall())
        mCallProxy!!.enqueue(OkHttpCallbackImpl(mCallProxy!!, callback))
    }

    /**
     * 取消网络请求
     */
    fun cancel() {
        mCallProxy?.cancel()
    }

    /**
     * 获取当前请求的 okhttp.Call
     */
    fun getRawCall(): Call {
        val requestBody = getRequestBody()
        val mRequest = getRequest(requestBody)
        return okHttpClient.newCall(mRequest)
    }

    /**
     * 生成一个 Request.Builder，并且给当前请求 Request 添加 headers
     */
    protected fun requestBuilder(): Request.Builder {
        val builder = Request.Builder()
        if (param.needHeaders) {
            for ((key, value) in param.headers.mHeaders) {
                builder.addHeader(key, value)
            }
        }
        return builder
    }

    open fun getRealUrl() = url

    open fun setTag(tag: Any): BaseRequest {
        setTag(tag.toString())
        return this
    }

    open fun setTag(tag: String): BaseRequest {
        this.tag = tag
        return this
    }
}