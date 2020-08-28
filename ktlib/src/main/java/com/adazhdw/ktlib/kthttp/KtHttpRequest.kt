package com.adazhdw.ktlib.kthttp

import android.os.Handler
import android.os.Looper
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.kthttp.callback.BaseRequestCallback
import com.adazhdw.ktlib.kthttp.constant.*
import com.adazhdw.ktlib.kthttp.param.KParams
import com.adazhdw.ktlib.kthttp.util.UrlUtil
import okhttp3.*
import java.io.IOException

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
        handleSuccess(response, callback)
    }

    override fun onFailure(call: Call, e: IOException) {
        handleError(e, callback)
    }

    private fun handleSuccess(response: Response, callback: BaseRequestCallback?) {
        val result = response.body?.string()
        if (result != null) {
            mHandler.post {
                callback?.onSuccess(result)
                KtHttpCallManager.instance.removeCall(url)
            }
        } else {
            handleError(Exception("response'body is null"), callback)
        }
    }

    private fun handleError(e: Exception, callback: BaseRequestCallback?) {
        mHandler.post {
            callback?.onError(e)
            KtHttpCallManager.instance.removeCall(url)
        }
    }

}