package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.kthttp.KtHttp.Companion.ktHttp
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.model.KtConfig
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest
import okhttp3.Call
import okhttp3.OkHttpClient

/**
 * author：daguozhu
 * date-time：2020/11/13 15:28
 * description：
 **/
class RequestCall(private val baseRequest: BaseRequest) {

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
        ktHttp.execute(this, callback)
        mCall = call
    }

    /**
     * 取消网络请求
     */
    fun cancel() {
        mCall?.cancel()
    }

}