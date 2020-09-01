package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.*
import com.adazhdw.ktlib.kthttp.param.KParams
import com.adazhdw.ktlib.kthttp.param.KtHttpRequest
import com.adazhdw.ktlib.kthttp.util.KtHttpCallManager
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType

object KtHttp {

    val JSON = "application/json; charset=utf-8".toMediaType()

    /**
     * 请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun request(
        method: Method,
        url: String,
        params: KParams? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(method, url, params ?: KParams(), callback)
    }

    /**
     * Get请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun get(
        url: String,
        params: KParams? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(GET, url, params ?: KParams(), callback)
    }

    /**
     * Post请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun post(
        url: String,
        params: KParams? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(POST, url, params ?: KParams(), callback)
    }

    /**
     * put请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun put(
        url: String,
        params: KParams? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(PUT, url, params ?: KParams(), callback)
    }

    /**
     * delete请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun delete(
        url: String,
        params: KParams? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(DELETE, url, params ?: KParams(), callback)
    }

    /**
     * head请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun head(
        url: String,
        params: KParams? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(HEAD, url, params ?: KParams(), callback)
    }

    /**
     * patch请求
     * @param url url
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun patch(
        url: String,
        params: KParams? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(PATCH, url, params ?: KParams(), callback)
    }

    private fun executeRequest(
        method: Method,
        url: String,
        params: KParams,
        callback: RequestCallback? = null
    ): Call {
        if (params.tag.isEmpty()) params.tag = url
        val request = KtHttpRequest(
            method = method,
            url = url,
            params = params,
            callback = callback
        )
        return request.execute()
    }

    fun cancel(url: String) {
        if (url.isNotBlank()) {
            val call: Call? = KtHttpCallManager.instance.getCall(url)
            call?.cancel()
            KtHttpCallManager.instance.removeCall(url)
        }
    }
}