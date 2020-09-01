package com.adazhdw.ktlib.kthttp.param

import android.os.Handler
import android.os.Looper
import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.*
import com.adazhdw.ktlib.kthttp.httpbuilder.KtHttpBuilder
import com.adazhdw.ktlib.kthttp.util.KtHttpCallManager
import com.adazhdw.ktlib.kthttp.util.KtUrlUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException

/**
 * author：daguozhu
 * date-time：2020/08/29 17:02
 * description：
 **/
class KtHttpRequest(
    val method: Method,
    val url: String,
    val params: KParams,
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
            GET -> {
                requestUrl = KtUrlUtil.getFullUrl(url, params.params, params.urlEncoder)
                requestBuilder.get()
            }
            DELETE -> {
                requestUrl = KtUrlUtil.getFullUrl(url, params.params, params.urlEncoder)
                requestBuilder.delete()
            }
            HEAD -> {
                requestUrl = KtUrlUtil.getFullUrl(url, params.params, params.urlEncoder)
                requestBuilder.head()
            }
            POST -> {
                val body = params.getRequestBody()
                requestBuilder.post(body)
            }
            PUT -> {
                val body = params.getRequestBody()
                requestBuilder.post(body)
            }
            PATCH -> {
                val body = params.getRequestBody()
                requestBuilder.post(body)
            }
        }
        addHeaders(requestBuilder, params.headers)
        requestBuilder.url(requestUrl).tag(params.tag)
        val request = requestBuilder.build()
        if (HttpConstant.debug) {
            "url:$requestUrl,params:${params.params},headers:${params.headers}".logD(url)
        }
        val call = KtHttpBuilder.okHttpClient.newCall(request = request)
        call.enqueue(this)
        KtHttpCallManager.instance.addCall(url, call)
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
        KtHttpCallManager.instance.removeCall(url)
        var timeout = e.message ?: ""
        if (e is SocketTimeoutException) {
            timeout = "request timeout"
        } else if (e is InterruptedIOException && e.message == "timeout") {
            timeout = "request timeout"
        }
        val code = HttpConstant.ERROR_RESPONSE_ON_FAILURE
        //回调跳转主线程
        mHandler.post {
            handleError(e, code, timeout, callback)
        }
    }

    private fun handleResponse(call: Call, response: Response, callback: RequestCallback?) {
        KtHttpCallManager.instance.removeCall(url)
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