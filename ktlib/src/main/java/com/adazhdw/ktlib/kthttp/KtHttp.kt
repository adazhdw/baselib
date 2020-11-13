package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.request.*
import okhttp3.Headers

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: 请求类
 */

class KtHttp private constructor() {

    companion object {
        val ktHttp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { KtHttp() }
    }

    private val commonParamBuilder = Params.Builder()
    private var commonParams: Params? = null

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
    ): RequestCall {
        return when (method) {
            Method.GET -> get(url, params, callback)
            Method.DELETE -> delete(url, params, callback)
            Method.HEAD -> head(url, params, callback)
            Method.POST -> post(url, params, callback)
            Method.PUT -> put(url, params, callback)
            Method.PATCH -> patch(url, params, callback)
        }
    }

    /**
     * Get请求
     * @param url url
     * @param params 请求参数
     */
    @JvmOverloads
    fun get(
        url: String,
        params: Params = Params(url),
        callback: RequestCallback? = null
    ): RequestCall {
        if (params.tag.isEmpty()) params.tag = url
        val request = RequestCall(GetRequest(url, params))
        request.execute(callback)
        return request
    }

    /**
     * Post请求
     * @param url url
     * @param params 请求参数
     */
    @JvmOverloads
    fun post(
        url: String,
        params: Params = Params(url),
        callback: RequestCallback? = null
    ): RequestCall {
        if (params.tag.isEmpty()) params.tag = url
        val request = RequestCall(PostRequest(url, params))
        request.execute(callback)
        return request
    }

    /**
     * put请求
     * @param url url
     * @param params 请求参数
     */
    @JvmOverloads
    fun put(
        url: String,
        params: Params = Params(url),
        callback: RequestCallback? = null
    ): RequestCall {
        if (params.tag.isEmpty()) params.tag = url
        val request = RequestCall(PutRequest(url, params))
        request.execute(callback)
        return request
    }

    /**
     * delete请求
     * @param url url
     * @param params 请求参数
     */
    @JvmOverloads
    fun delete(
        url: String,
        params: Params = Params(url),
        callback: RequestCallback? = null
    ): RequestCall {
        if (params.tag.isEmpty()) params.tag = url
        val request = RequestCall(DeleteRequest(url, params))
        request.execute(callback)
        return request
    }

    /**
     * head请求
     * @param url url
     * @param params 请求参数
     */
    @JvmOverloads
    fun head(
        url: String,
        params: Params = Params(url),
        callback: RequestCallback? = null
    ): RequestCall {
        if (params.tag.isEmpty()) params.tag = url
        val request = RequestCall(HeadRequest(url, params))
        request.execute(callback)
        return request
    }

    /**
     * patch请求
     * @param url url
     * @param params 请求参数
     */
    @JvmOverloads
    fun patch(
        url: String,
        params: Params = Params(url),
        callback: RequestCallback? = null
    ): RequestCall {
        if (params.tag.isEmpty()) params.tag = url
        val request = RequestCall(PatchRequest(url, params))
        request.execute(callback)
        return request
    }

    /**
     * 设置 公共 header 参数
     */
    fun addCommonHeaders(headers: Map<String, String>): KtHttp {
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
    fun getCommonHttpHeaders(): Headers {
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
}