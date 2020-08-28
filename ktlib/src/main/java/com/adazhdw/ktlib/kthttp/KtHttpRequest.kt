package com.adazhdw.ktlib.kthttp

import android.os.Handler
import android.os.Looper
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.kthttp.bean.ResponseData
import com.adazhdw.ktlib.kthttp.callback.BaseRequestCallback
import com.adazhdw.ktlib.kthttp.constant.*
import com.adazhdw.ktlib.kthttp.param.KParams
import com.adazhdw.ktlib.kthttp.util.UrlUtil
import okhttp3.*
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException

class KtHttpRequest(
    val method: Method,
    val url: String,
    val params: KParams,
    val builder: OkHttpClient.Builder,
    val callback: BaseRequestCallback?
) : Callback {

    private val okHttpClient: OkHttpClient = builder.build()
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    fun execute(): Call {
        callback?.onStart()
        var requestUrl: String = url
        val requestBuilder = Request.Builder()
        when (method) {
            GET -> {
                requestUrl = UrlUtil.getFullUrl(url, params.params, params.urlEncoder)
                requestBuilder.get()
            }
            DELETE -> {
                requestUrl = UrlUtil.getFullUrl(url, params.params, params.urlEncoder)
                requestBuilder.delete()
            }
            HEAD -> {
                requestUrl = UrlUtil.getFullUrl(url, params.params, params.urlEncoder)
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
        val call = okHttpClient.newCall(request = request)
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
        handleResponse(ResponseData(), response)
    }

    override fun onFailure(call: Call, e: IOException) {
        val responseData = ResponseData()
        if (e is SocketTimeoutException) {
            responseData.timeout = true
        } else if (e is InterruptedIOException && e.message == "timeout") {
            responseData.timeout = true
        }
        responseData.msg = e.message ?: ""
        handleResponse(responseData, null)
    }

    private fun handleResponse(responseData: ResponseData, response: Response?) {
        if (response != null) {
            responseData.responseNull = false
            responseData.code = response.code
            responseData.msg = response.message
            responseData.successful = response.isSuccessful
            var responseBody: String? = null
            try {
                responseBody = response.body?.string()
            } catch (e: IOException) {
                responseData.code = HttpConstant.ERROR_RESPONSE_BODY_ISNULL
                responseData.msg = "response'body is null"
            }
            if (!responseBody.isNullOrBlank()) {
                responseData.result = responseBody
            } else {
                responseData.code = HttpConstant.ERROR_RESPONSE_BODY_ISNULL
                responseData.msg = "response'body is null"
            }
            responseData.headers = response.headers
            responseData.httpResponse = response
        } else {
            responseData.responseNull = true
            responseData.code = HttpConstant.ERROR_RESPONSE_ON_FAILURE
            if (responseData.timeout) {
                responseData.msg = "request timeout"
            }
        }
        //回调跳转主线程
        mHandler.post {
            executeResponse(responseData)
        }
    }

    private fun executeResponse(responseData: ResponseData) {

    }

    private fun handleSuccess(response: Response, callback: BaseRequestCallback?) {
        val result = response.body?.string()
        callback?.onResponse(response, result, response.headers)
        if (result != null) {//请求得到响应
            mHandler.post {
                callback?.onSuccess(result)
                callback?.onFinish()
                KtHttpCallManager.instance.removeCall(url)
            }
        } else {
            handleError(Exception("response'body is null"), callback)
        }
    }

    private fun handleError(e: Exception, callback: BaseRequestCallback?) {
        mHandler.post {
            callback?.onError(e)
            callback?.onFinish()
            KtHttpCallManager.instance.removeCall(url)
        }
    }

}