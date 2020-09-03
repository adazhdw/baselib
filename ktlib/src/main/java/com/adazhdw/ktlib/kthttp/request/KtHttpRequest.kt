package com.adazhdw.ktlib.kthttp.request

import android.os.Handler
import android.os.Looper
import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.kthttp.KtHttp
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.HttpConstant
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.exception.NetWorkUnAvailableException
import com.adazhdw.ktlib.kthttp.param.Param
import com.adazhdw.ktlib.kthttp.util.OkHttpCallManager
import com.adazhdw.ktlib.kthttp.util.RequestUrlUtil
import com.adazhdw.ktlib.utils.NetworkUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * author：daguozhu
 * date-time：2020/08/29 17:02
 * description：
 **/
class KtHttpRequest(
    val method: Method,
    val url: String,
    val param: Param,
    val callback: RequestCallback?
) : Callback {

    companion object {
        private val mHandler: Handler = Handler(Looper.getMainLooper())
    }

    fun execute(): Call {
        callback?.onStart()
        var requestUrl: String = url
        val requestBuilder = Request.Builder()
        when (method) {
            Method.GET -> {
                requestUrl = RequestUrlUtil.getFullUrl(url, param.params, param.urlEncoder)
                requestBuilder.get()
            }
            Method.DELETE -> {
                requestUrl = RequestUrlUtil.getFullUrl(url, param.params, param.urlEncoder)
                requestBuilder.delete()
            }
            Method.HEAD -> {
                requestUrl = RequestUrlUtil.getFullUrl(url, param.params, param.urlEncoder)
                requestBuilder.head()
            }
            Method.POST -> {
                val body = param.getRequestBody()
                requestBuilder.post(body)
            }
            Method.PUT -> {
                val body = param.getRequestBody()
                requestBuilder.post(body)
            }
            Method.PATCH -> {
                val body = param.getRequestBody()
                requestBuilder.post(body)
            }
        }
        addHeaders(requestBuilder, param.headers)
        requestBuilder.url(requestUrl).tag(param.tag)
        val request = requestBuilder.build()
        if (HttpConstant.debug) {
            "url:$requestUrl,params:${param.params},headers:${param.headers}".logD(url)
        }
        val call = KtHttp.okHttpClient.newCall(request = request)
        call.enqueue(this)
        OkHttpCallManager.instance.addCall(url, call)
        return call
    }

    private fun addHeaders(
        requestBuilder: Request.Builder,
        headers: Map<String, String>
    ) {
        if (headers.isEmpty()) return
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }
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
            handleError(ex, code, message, callback)
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
                    handleError(
                        KotlinNullPointerException(errorMsg),
                        HttpConstant.ERROR_RESPONSE_BODY_ISNULL,
                        "response'body is null",
                        callback
                    )
                }
            }
        }
    }

    private fun handleError(e: Exception, code: Int, msg: String?, callback: RequestCallback?) {
        callback?.onError(e, code, msg)
        callback?.onFinish()
    }

}