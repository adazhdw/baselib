package com.adazhdw.kthttp.request.base

import com.adazhdw.kthttp.KtConfig
import com.adazhdw.kthttp.callback.OkHttpCallbackImpl
import com.adazhdw.kthttp.callback.RequestCallback
import com.adazhdw.kthttp.entity.Param
import com.adazhdw.kthttp.request.CallProxy
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
    private var mCallProxy: CallProxy? = null
    private var mCall: Call? = null
    protected var tag = ""

    abstract fun getRequestBody(): RequestBody

    abstract fun getRequest(requestBody: RequestBody): Request

    /**
     * 执行网络请求
     */
    fun execute(callback: RequestCallback?) {
        mCallProxy = CallProxy(getRawCall())
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
    internal fun getRawCall(): Call {
        if (mCall == null) {
            val requestBody = getRequestBody()
            val mRequest = getRequest(requestBody)
            mCall = okHttpClient.newCall(mRequest)
        }
        return mCall!!
    }

    /**
     * 生成一个 Request.Builder，并且给当前请求 Request 添加 headers
     */
    protected fun requestBuilder(): Request.Builder {
        val builder = Request.Builder()
        if (param.needHeaders) {
            for ((key, value) in param.headers()) {
                builder.addHeader(key, value)
            }
        }
        return builder
    }

    open fun getRealUrl() = url

    open fun tag(tag: Any?): BaseRequest {
        this.tag(tag.toString())
        return this
    }

    open fun tag(tag: String): BaseRequest {
        this.tag = tag
        return this
    }
}