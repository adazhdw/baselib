package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.ktlib.kthttp.request.*
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest
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

    private val mParams: HashMap<String, String> = hashMapOf()
    private val mHeaders: HashMap<String, String> = hashMapOf()

    /**
     * 请求
     * @param url url
     * @param param 请求参数工具类
     * @param callback 请求回调
     */
    @JvmOverloads
    fun request(
        method: Method,
        url: String,
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): BaseRequest {
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
     * @param param 请求参数工具类
     */
    @JvmOverloads
    fun get(
        url: String,
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): BaseRequest {
        val request = GetRequest(url, param)
        request.execute(callback)
        return request
    }

    /**
     * Post请求
     * @param url url
     * @param param 请求参数工具类
     */
    @JvmOverloads
    fun post(
        url: String,
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): BaseRequest {
        val request = PostRequest(url, param)
        request.execute(callback)
        return request
    }

    /**
     * delete请求
     * @param url url
     * @param param 请求参数工具类
     */
    @JvmOverloads
    fun delete(
        url: String,
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): BaseRequest {
        val request = DeleteRequest(url, param)
        request.execute(callback)
        return request
    }

    /**
     * head请求
     * @param url url
     * @param param 请求参数工具类
     */
    @JvmOverloads
    fun head(
        url: String,
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): BaseRequest {
        val request = HeadRequest(url, param)
        request.execute(callback)
        return request
    }

    /**
     * put请求
     * @param url url
     * @param param 请求参数工具类
     */
    @JvmOverloads
    fun put(
        url: String,
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): BaseRequest {
        val request = PutRequest(url, param)
        request.execute(callback)
        return request
    }

    /**
     * patch请求
     * @param url url
     * @param param 请求参数工具类
     */
    @JvmOverloads
    fun patch(
        url: String,
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): BaseRequest {
        val request = PatchRequest(url, param)
        request.execute(callback)
        return request
    }

    /**
     * 设置 公共 header 参数
     */
    fun addCommonHeaders(headers: Map<String, String>): KtHttp {
        mHeaders.putAll(headers)
        return this
    }

    /**
     * 获取 公共 header 参数
     */
    fun getCommonHeaders(): HashMap<String, String> {
        return mHeaders
    }

    /**
     * 获取 公共 header 参数
     */
    fun getCommonHttpHeaders(): Headers {
        val headers = Headers.Builder()
        for ((name, value) in mHeaders) {
            headers.add(name, value)
        }
        return headers.build()
    }

    /**
     * 设置 公共参数
     */
    fun setCommonParams(params: Map<String, String>): KtHttp {
        mParams.putAll(params)
        return this
    }

    /**
     * 获取 公共参数
     */
    fun getCommonParams(): HashMap<String, String> {
        return mParams
    }

    @JvmOverloads
    fun get(url: String, param: Param = Param.build()): GetRequest = GetRequest(url, param)

    @JvmOverloads
    fun post(url: String, param: Param = Param.build()): PostRequest = PostRequest(url, param)

    @JvmOverloads
    fun delete(url: String, param: Param = Param.build()): DeleteRequest = DeleteRequest(url, param)

    @JvmOverloads
    fun head(url: String, param: Param = Param.build()): HeadRequest = HeadRequest(url, param)

    @JvmOverloads
    fun put(url: String, param: Param = Param.build()): PutRequest = PutRequest(url, param)

    @JvmOverloads
    fun patch(url: String, param: Param = Param.build()): PatchRequest = PatchRequest(url, param)


}