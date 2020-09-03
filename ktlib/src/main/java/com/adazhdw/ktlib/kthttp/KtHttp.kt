package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.BuildConfig
import com.adazhdw.ktlib.http.OkHttpLogger
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.HttpConstant
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.interceptor.RetryInterceptor
import com.adazhdw.ktlib.kthttp.param.Param
import com.adazhdw.ktlib.kthttp.request.GetRequest
import com.adazhdw.ktlib.kthttp.request.KtHttpRequest
import com.adazhdw.ktlib.kthttp.request.PostRequest
import com.adazhdw.ktlib.kthttp.ssl.HttpsUtils
import com.adazhdw.ktlib.kthttp.util.OkHttpCallManager
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: 请求类
 */

object KtHttp {

    val JSON = "application/json; charset=utf-8".toMediaType()
    val PNG = "image/png; charset=UTF-8".toMediaType()
    val JPG = "image/jpeg; charset=UTF-8".toMediaType()

    val okHttpClient: OkHttpClient by lazy { obtainBuilder().build() }

    private fun obtainBuilder(timeout: Long = HttpConstant.DEFAULT_TIMEOUT): OkHttpClient.Builder {
        val sslParams = HttpsUtils.getSslSocketFactory()
        return OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .callTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor(RetryInterceptor())
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(OkHttpLogger()).apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
    }

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
        return when (method) {
            Method.GET -> get(url, param, callback)
            Method.DELETE -> delete(url, param, callback)
            Method.HEAD -> head(url, param, callback)
            Method.POST -> post(url, param, callback)
            Method.PUT -> put(url, param, callback)
            Method.PATCH -> patch(url, param, callback)
        }
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
        return GetRequest(url, param ?: Param(), callback).execute()
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
        return PostRequest(url, param ?: Param(), callback).execute()
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
        return executeRequest(Method.PUT, url, param ?: Param(), callback)
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
        return executeRequest(Method.DELETE, url, param ?: Param(), callback)
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
        return executeRequest(Method.HEAD, url, param ?: Param(), callback)
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
        return executeRequest(Method.PATCH, url, param ?: Param(), callback)
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