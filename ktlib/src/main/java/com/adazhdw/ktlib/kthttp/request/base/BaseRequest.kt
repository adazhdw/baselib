package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.core.KtExecutors.networkIO
import com.adazhdw.ktlib.kthttp.KtHttp.Companion.ktHttp
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.exception.ExceptionHelper
import com.adazhdw.ktlib.kthttp.exception.HttpStatusException
import com.adazhdw.ktlib.kthttp.exception.NetWorkUnAvailableException
import com.adazhdw.ktlib.kthttp.model.HttpConstant
import com.adazhdw.ktlib.kthttp.model.HttpConstant.ERROR_RESPONSE_NORMAL_ERROR
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.util.OkHttpCallManager.Companion.callManager
import com.adazhdw.ktlib.utils.NetworkUtils
import okhttp3.*
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
    private val okHttpClient: OkHttpClient = ktHttp.mOkHttpClient
    private val commonHeaders = ktHttp.getCommonHeaders()
    var mCall: Call? = null
        private set

    abstract fun getRequestBody(): RequestBody

    abstract fun getRequest(requestBody: RequestBody): Request

    /**
     * 获取当前请求的 okhttp.Call
     */
    private fun getRawCall(): Call {
        val requestBody = getRequestBody()
        val mRequest = getRequest(requestBody)
        return okHttpClient.newCall(mRequest)
    }

    /**
     * 生成一个 Request.Builder，并且给当前请求 Request 添加 headers
     */
    protected fun requestBuilder(): Request.Builder {
        val builder = Request.Builder()
        if (params.needHeaders) {
            if (commonHeaders.isNotEmpty()) builder.headers(ktHttp.getCommonHttpHeaders())
            for ((key, value) in params.headers) builder.addHeader(key, value)
        }
        return builder
    }

    /**
     * 执行网络请求
     */
    @Suppress("UNCHECKED_CAST")
    fun execute(callback: RequestCallback?) {
        val call = getRawCall()
        if (callManager.addCall(url, params, call)) {//添加成功说明没有重复请求
            callback?.onStart(call)
            call.enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    callManager.removeCall(url, params)
                    handleResponse(response, callback)
                }

                override fun onFailure(call: Call, e: IOException) {
                    callManager.removeCall(url, params)
                    e.printStackTrace()
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
                    ktHttp.mHandler.post { handleFailure(ex, code, message, callback) }
                }
            })
            mCall = call
        }
    }

    /**
     * 取消网络请求
     */
    fun cancel() {
        mCall?.cancel()
        callManager.removeCall(url, params)
    }

    private fun handleResponse(response: Response, callback: RequestCallback?) {
        networkIO.submit {
            try {
                val result = ExceptionHelper.getNotNullResult(response).string()
                //回调跳转主线程
                ktHttp.mHandler.post {
                    callback?.onHttpResponse(response, result)
                    callback?.onFinish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ktHttp.mHandler.post {
                    if (e is HttpStatusException) {
                        handleFailure(e, e.statusCode, e.message, callback)
                    } else {
                        handleFailure(e, ERROR_RESPONSE_NORMAL_ERROR, e.message, callback)
                    }
                }
            }
        }
    }

    private fun handleFailure(e: Exception, code: Int, msg: String?, callback: RequestCallback?) {
        callback?.onFailure(e, code, msg)
        callback?.onFinish()
    }

}