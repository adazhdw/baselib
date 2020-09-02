package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.*
import com.adazhdw.ktlib.kthttp.param.Param
import com.adazhdw.ktlib.kthttp.util.OkHttpCallManager
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: 请求类
 */

object KtHttp {

    val JSON = "application/json; charset=utf-8".toMediaType()
    val PNG = "image/png; charset=UTF-8".toMediaType()
    val JPG = "image/jpeg; charset=UTF-8".toMediaType()

    /**
     * 请求
     * @param url url
     * @param param 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun request(
        method: Method,
        url: String,
        param: Param? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(method, url, param ?: Param(), callback)
    }

    /**
     * Get请求
     * @param url url
     * @param param 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun get(
        url: String,
        param: Param? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(GET, url, param ?: Param(), callback)
    }

    /**
     * Post请求
     * @param url url
     * @param param 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun post(
        url: String,
        param: Param? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(POST, url, param ?: Param(), callback)
    }

    /**
     * put请求
     * @param url url
     * @param param 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun put(
        url: String,
        param: Param? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(PUT, url, param ?: Param(), callback)
    }

    /**
     * delete请求
     * @param url url
     * @param param 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun delete(
        url: String,
        param: Param? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(DELETE, url, param ?: Param(), callback)
    }

    /**
     * head请求
     * @param url url
     * @param param 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun head(
        url: String,
        param: Param? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(HEAD, url, param ?: Param(), callback)
    }

    /**
     * patch请求
     * @param url url
     * @param param 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun patch(
        url: String,
        param: Param? = null,
        callback: RequestCallback? = null
    ): Call {
        return executeRequest(PATCH, url, param ?: Param(), callback)
    }

    private fun executeRequest(
        method: Method,
        url: String,
        param: Param,
        callback: RequestCallback? = null
    ): Call {
        if (param.tag.isEmpty()) param.tag = url
        val request = KtHttpRequest(
            method = method,
            url = url,
            param = param,
            callback = callback
        )
        return request.execute()
    }

    fun cancel(url: String) {
        if (url.isNotBlank()) {
            val call: Call? = OkHttpCallManager.instance.getCall(url)
            call?.cancel()
            OkHttpCallManager.instance.removeCall(url)
        }
    }
}