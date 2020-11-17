package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.exception.ExceptionHelper
import com.adazhdw.ktlib.kthttp.exception.HttpStatusException
import com.adazhdw.ktlib.kthttp.exception.NetWorkUnAvailableException
import com.adazhdw.ktlib.kthttp.model.HttpConstant
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.ktlib.kthttp.request.*
import com.adazhdw.ktlib.utils.NetworkUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.Response
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
     * @param params 请求参数
     * @param callback 请求回调
     */
    @JvmOverloads
    fun request(
        method: Method,
        url: String,
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): RequestCall {
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
     * @param params 请求参数
     */
    @JvmOverloads
    fun get(
        url: String,
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): RequestCall {
        val request = RequestCall(GetRequest(url, param))
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
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): RequestCall {
        val request = RequestCall(PostRequest(url, param))
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
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): RequestCall {
        val request = RequestCall(PutRequest(url, param))
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
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): RequestCall {
        val request = RequestCall(DeleteRequest(url, param))
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
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): RequestCall {
        val request = RequestCall(HeadRequest(url, param))
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
        param: Param = Param.build(),
        callback: RequestCallback? = null
    ): RequestCall {
        val request = RequestCall(PatchRequest(url, param))
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

    /**
     * 网络请求具体执行处理方法
     */
    fun execute(requestCall: RequestCall, callback: RequestCallback?) {
        val call = requestCall.mCall ?: return
        callback?.onStart(call)
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                handleResponse(response, callback)
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                var ex: Exception = e
                var message = e.message ?: ""
                var code = HttpConstant.ERROR_RESPONSE_ON_FAILURE
                if (e is SocketTimeoutException) {
                    message = "request timeout"
                } else if (e is InterruptedIOException && e.message == "timeout") {
                    message = "request timeout"
                } else if (e is UnknownHostException && !NetworkUtil.isConnected()) {
                    message = "network unavailable"
                    code = HttpConstant.ERROR_NETWORK_UNAVAILABLE
                    ex = NetWorkUnAvailableException()
                } else if (e.message == "Canceled") {
                    code = HttpConstant.ERROR_REQUEST_CANCEL_ERROR
                    message = "request canceled"
                }
                //回调跳转主线程
                KtExecutors.mainThread.execute { handleFailure(ex, code, message, callback) }
            }
        })
    }

    private fun handleResponse(response: Response, callback: RequestCallback?) {
        KtExecutors.networkIO.submit {
            try {
                val result = ExceptionHelper.getNotNullResult(response).string()
                //回调跳转主线程
                KtExecutors.mainThread.execute {
                    callback?.onHttpResponse(response, result)
                    callback?.onFinish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                KtExecutors.mainThread.execute {
                    if (e is HttpStatusException) {
                        handleFailure(e, e.statusCode, e.message, callback)
                    } else {
                        val code = HttpConstant.ERROR_RESPONSE_NORMAL_ERROR
                        handleFailure(e, code, e.message, callback)
                    }
                }
            }
        }
    }

    private fun handleFailure(e: Exception, code: Int, msg: String?, callback: RequestCallback?) {
        callback?.onFailure(e, code, msg)
        callback?.onFinish()
    }

}