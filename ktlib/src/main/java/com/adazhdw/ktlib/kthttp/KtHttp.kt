package com.adazhdw.ktlib.kthttp

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.ktlib.kthttp.request.*
import okhttp3.Call
import okhttp3.Headers

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: 请求工具类
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
        callback: RequestCallback
    ) {
        when (method) {
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
        callback: RequestCallback
    ) {
        get(url, param).tag(callback.mLifecycleOwner).execute(callback)
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
        callback: RequestCallback
    ) {
        post(url, param).tag(callback.mLifecycleOwner).execute(callback)
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
        callback: RequestCallback
    ) {
        delete(url, param).tag(callback.mLifecycleOwner).execute(callback)
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
        callback: RequestCallback
    ) {
        head(url, param).tag(callback.mLifecycleOwner).execute(callback)
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
        callback: RequestCallback
    ) {
        put(url, param).tag(callback.mLifecycleOwner).execute(callback)
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
        callback: RequestCallback
    ) {
        patch(url, param).tag(callback.mLifecycleOwner).execute(callback)
    }

    /**
     * 取消请求
     */
    fun cancel(lifecycleOwner: LifecycleOwner) {
        cancel(lifecycleOwner.toString())
    }

    /**
     * 根据 TAG 取消请求任务
     */
    fun cancel(tag: Any?) {
        if (tag == null) return
        val client = KtConfig.mOkHttpClient

        //清除排队的请求任务
        for (call: Call in client.dispatcher.queuedCalls()) {
            if (tag == call.request().tag()) {
                call.cancel()
            }
        }

        //清除正在执行的任务
        for (call: Call in client.dispatcher.runningCalls()) {
            if (tag == call.request().tag()) {
                call.cancel()
            }
        }
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