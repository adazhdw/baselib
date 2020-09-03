package com.adazhdw.ktlib.kthttp.request.base

import android.os.Handler
import android.os.Looper
import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.HttpConstant
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.exception.NetWorkUnAvailableException
import com.adazhdw.ktlib.kthttp.param.Param
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
    val param: Param,
    val callback: RequestCallback?
) : Callback {
    abstract val okHttpClient: OkHttpClient

    companion object {
        private val mHandler: Handler = Handler(Looper.getMainLooper())
    }

    abstract fun getRequestBody(): RequestBody

    abstract fun obtainRequest(requestBody: RequestBody): Request

    private fun getRawCall(): Call {
        val requestBody = getRequestBody()
        val mRequest = obtainRequest(requestBody)
        return okHttpClient.newCall(mRequest)
    }

    fun addHeaders(builder: Request.Builder, headers: Map<String, String>): Request.Builder {
        for ((key, value) in headers) {
            builder.addHeader(key, value)
        }
        return builder
    }

    fun execute(): Call {
        val call = getRawCall()
        call.enqueue(this)
        OkHttpCallManager.instance.addCall(url, call)
        return call
    }

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
        mHandler.post {
            handleFailure(ex, code, message, callback)
        }
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
            mHandler.post { callback?.onResponse(response, result, response.headers) }
            if (!result.isNullOrBlank()) {
                //回调跳转主线程
                mHandler.post {
                    callback?.onSuccess(result)
                    callback?.onFinish()
                }
            } else {
                val invocation = call.request().tag(Invocation::class.java)
                val method = invocation?.method()
                val errorMsg =
                    "Response from ${method?.declaringClass?.name}.${method?.name} was null but response body type was declared as non-null"
                mHandler.post {
                    handleFailure(
                        KotlinNullPointerException(errorMsg),
                        HttpConstant.ERROR_RESPONSE_BODY_ISNULL,
                        "response'body is null",
                        callback
                    )
                }
            }
        }
    }

    private fun handleFailure(e: Exception, code: Int, msg: String?, callback: RequestCallback?) {
        callback?.onError(e, code, msg)
        callback?.onFinish()
    }

}