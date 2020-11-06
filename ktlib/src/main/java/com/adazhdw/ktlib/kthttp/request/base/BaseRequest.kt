package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.kthttp.KtHttp
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.exception.NetWorkUnAvailableException
import com.adazhdw.ktlib.kthttp.model.HttpConstant
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.util.OkHttpCallManager
import com.adazhdw.ktlib.utils.NetworkUtils
import okhttp3.*
import retrofit2.Invocation
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * author：daguozhu
 * date-time：2020/9/3 10:11
 * description：
 **/
abstract class BaseRequest<R : BaseRequest<R>>(
    val method: Method,
    val url: String,
    val params: Params
) {
    private val okHttpClient: OkHttpClient = KtHttp.ktHttp.mOkHttpClient
    private val commonHeaders = KtHttp.ktHttp.getCommonHeaders()
    var mCall: Call? = null
        private set

    abstract fun getRequestBody(): RequestBody

    abstract fun obtainRequest(requestBody: RequestBody): Request

    /**
     * 获取当前请求的 okhttp.Call
     */
    private fun getRawCall(): Call {
        val requestBody = getRequestBody()
        val mRequest = obtainRequest(requestBody)
        return okHttpClient.newCall(mRequest)
    }

    /**
     * 生成一个 Request.Builder，并且给当前请求 Request 添加 headers
     */
    protected fun requestBuilder(): Request.Builder {
        val builder = Request.Builder()
        if (commonHeaders.isNotEmpty()) builder.headers(KtHttp.ktHttp.getHttpHeaders())
        for ((key, value) in params.headers) builder.addHeader(key, value)
        return builder
    }

    /**
     * 执行网络请求
     */
    @Suppress("UNCHECKED_CAST")
    fun execute(callback: RequestCallback?): R {
        val call = getRawCall()
        callback?.onStart(call)
        OkHttpCallManager.instance.addCall(url, call)
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                handleResponse(call, response, callback)
            }

            override fun onFailure(call: Call, e: IOException) {
                OkHttpCallManager.instance.removeCall(url)
                var ex: Exception = e
                var message = e.message ?: ""
                var code = HttpConstant.ERROR_RESPONSE_ON_FAILURE
                if (e is SocketTimeoutException) {
                    message = "request timeout"
                } else if (e is InterruptedIOException && e.message == "timeout") {
                    message = "request timeout"
                } else if (e is UnknownHostException && !NetworkUtils.isConnected()) {
                    message = "network unavailable"
                    code = HttpConstant.ERROR_NETWORK_UNAVAILABLE
                    ex = NetWorkUnAvailableException()
                }
                //回调跳转主线程
                KtHttp.ktHttp.mHandler.post {
                    handleFailure(ex, code, message, callback)
                }
            }
        })
        mCall = call
        return this as R
    }

    /**
     * 取消网络请求
     */
    fun cancel() {
        if (mCall?.isCanceled() == false) mCall?.cancel()
    }

    private fun handleResponse(call: Call, response: Response, callback: RequestCallback?) {
        OkHttpCallManager.instance.removeCall(url)
        KtExecutors.networkIO.submit {
            var result: String? = null
            try {
                result = response.body?.string()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            KtHttp.ktHttp.mHandler.post { callback?.onHttpResponse(response, result) }
            if (!result.isNullOrBlank()) {
                //回调跳转主线程
                KtHttp.ktHttp.mHandler.post {
                    callback?.onResponse(result)
                    callback?.onFinish()
                }
            } else {
                val invocation = call.request().tag(Invocation::class.java)
                val method = invocation?.method()
                KtHttp.ktHttp.mHandler.post {
                    handleFailure(
                        Exception("Response body from ${method?.declaringClass?.name}.${method?.name} is null"),
                        HttpConstant.ERROR_RESPONSE_BODY_ISNULL,
                        "response'body is null", callback
                    )
                }
            }
        }
    }

    private fun handleFailure(e: Exception, code: Int, msg: String?, callback: RequestCallback?) {
        callback?.onFailure(e, code, msg)
        callback?.onFinish()
    }

}