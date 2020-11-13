package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.exception.ExceptionHelper
import com.adazhdw.ktlib.kthttp.exception.HttpStatusException
import com.adazhdw.ktlib.kthttp.exception.NetWorkUnAvailableException
import com.adazhdw.ktlib.kthttp.model.HttpConstant
import com.adazhdw.ktlib.kthttp.model.KtConfig
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest
import com.adazhdw.ktlib.kthttp.util.OkHttpCallManager
import com.adazhdw.ktlib.utils.NetworkUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * author：daguozhu
 * date-time：2020/11/13 15:28
 * description：
 **/
class RequestCall(
    private val baseRequest: BaseRequest,
    private val url: String = baseRequest.url,
    private val params: Params = baseRequest.params
) {

    private val okHttpClient: OkHttpClient = KtConfig.getOkHttpClient()
    var mCall: Call? = null
        private set

    /**
     * 获取当前请求的 okhttp.Call
     */
    private fun getRawCall(): Call {
        val requestBody = baseRequest.getRequestBody()
        val mRequest = baseRequest.getRequest(requestBody)
        return okHttpClient.newCall(mRequest)
    }

    /**
     * 执行网络请求
     */
    @Suppress("UNCHECKED_CAST")
    fun execute(callback: RequestCallback?) {
        val call = getRawCall()
        if (OkHttpCallManager.callManager.addCall(url, params, call)) {//添加成功说明没有重复请求
            callback?.onStart(call)
            call.enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    OkHttpCallManager.callManager.removeCall(url, params)
                    handleResponse(response, callback)
                }

                override fun onFailure(call: Call, e: IOException) {
                    OkHttpCallManager.callManager.removeCall(url, params)
                    e.printStackTrace()
                    var ex: Exception = e
                    var message = e.message ?: ""
                    var code = HttpConstant.ERROR_RESPONSE_ON_FAILURE
                    if (e is SocketTimeoutException) {
                        message = "request timeout"
                    } else if (e is InterruptedIOException && e.message == "timeout") {
                        message = "request timeout"
                    } else if (e is UnknownHostException && !NetworkUtils.isConnected()) {
                        message = "network unavailable"
                        code = HttpConstant.ERROR_NETWORK_UNAVAILABLE
                        ex = NetWorkUnAvailableException()
                    }
                    //回调跳转主线程
                    KtExecutors.mainThread.execute { handleFailure(ex, code, message, callback) }
                }
            })
            mCall = call
        }
    }

    /**
     * 取消网络请求
     */
    fun cancel() {
        mCall?.cancel()
        OkHttpCallManager.callManager.removeCall(url, params)
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
                        handleFailure(
                            e,
                            HttpConstant.ERROR_RESPONSE_NORMAL_ERROR, e.message, callback
                        )
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