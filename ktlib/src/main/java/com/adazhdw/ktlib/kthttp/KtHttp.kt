package com.adazhdw.ktlib.kthttp

import android.os.Handler
import android.os.Looper
import com.adazhdw.ktlib.BuildConfig
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.interceptor.RetryInterceptor
import com.adazhdw.ktlib.kthttp.model.HttpConstant
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.request.*
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest
import com.adazhdw.ktlib.kthttp.ssl.HttpsUtils
import com.adazhdw.ktlib.kthttp.util.OkHttpCallManager
import com.adazhdw.ktlib.kthttp.util.OkHttpLogger
import okhttp3.Call
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: 请求类
 */

class KtHttp private constructor() {

    internal var mOkHttpClient: OkHttpClient = obtainClient()
    internal val mHandler: Handler = Handler(Looper.getMainLooper())
    private val commonParamBuilder = Params.Builder()
    private var commonParams: Params? = null

    companion object {
        fun getInstance() = ktHttp

        private val ktHttp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { KtHttp() }

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
            params: Params = Params(url),
            callback: RequestCallback? = null
        ): BaseRequest<*> {
            val request = when (method) {
                Method.GET -> get(url, params)
                Method.DELETE -> delete(url, params)
                Method.HEAD -> head(url, params)
                Method.POST -> post(url, params)
                Method.PUT -> put(url, params)
                Method.PATCH -> patch(url, params)
            }
            return request.execute(callback)
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
            params: Params = Params(url)
        ): GetRequest {
            if (params.tag.isEmpty()) params.tag = url
            return GetRequest(url, params)
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
            params: Params = Params(url)
        ): PostRequest {
            if (params.tag.isEmpty()) params.tag = url
            return PostRequest(url, params)
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
            params: Params = Params(url)
        ): PutRequest {
            if (params.tag.isEmpty()) params.tag = url
            return PutRequest(url, params)
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
            params: Params = Params(url)
        ): DeleteRequest {
            if (params.tag.isEmpty()) params.tag = url
            return DeleteRequest(url, params)
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
            params: Params = Params(url)
        ): HeadRequest {
            if (params.tag.isEmpty()) params.tag = url
            return HeadRequest(url, params)
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
            params: Params = Params(url)
        ): PatchRequest {
            if (params.tag.isEmpty()) params.tag = url
            return PatchRequest(url, params)
        }

        fun cancel(url: String) {
            if (url.isNotBlank()) {
                val call: Call? = OkHttpCallManager.instance.getCall(url)
                call?.cancel()
                OkHttpCallManager.instance.removeCall(url)
            }
        }

    }

    /**
     * 设置 OkHttpClient
     */
    fun setOkHttpClient(client: OkHttpClient): KtHttp {
        this.mOkHttpClient = client
        return this
    }

    /**
     * 设置 公共 header 参数
     */
    fun setCommonHeaders(headers: Map<String, String>): KtHttp {
        commonParamBuilder.addHeaders(headers)
        return this
    }

    /**
     * 获取 公共 header 参数
     */
    fun getCommonHeaders(): Map<String, String> {
        if (commonParams == null) {
            commonParams = commonParamBuilder.build()
        }
        return commonParams?.headers ?: mapOf()
    }

    /**
     * 获取 公共 header 参数
     */
    fun getHttpHeaders(): Headers {
        if (commonParams == null) {
            commonParams = commonParamBuilder.build()
        }
        val headers = Headers.Builder()
        for ((name, value) in commonParams!!.headers) {
            headers.add(name, value)
        }
        return headers.build()
    }

    /**
     * 设置 公共参数
     */
    fun setCommonParams(params: Map<String, String>): KtHttp {
        commonParamBuilder.addParams(params)
        return this
    }

    /**
     * 获取 公共参数
     */
    fun getCommonParams(): Map<String, String> {
        if (commonParams == null) {
            commonParams = commonParamBuilder.build()
        }
        return commonParams?.params ?: mapOf()
    }

    /**
     * 获取内置 okHttpClient
     */
    private fun obtainClient(timeout: Long = HttpConstant.DEFAULT_TIMEOUT): OkHttpClient {
        val sslParams = HttpsUtils.getSslSocketFactory()
        return OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .callTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor(RetryInterceptor())
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).build()
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

}