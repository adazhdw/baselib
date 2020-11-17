package com.adazhdw.ktlib.kthttp.callback

import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.ext.logE
import okhttp3.Call
import okhttp3.Response

/**
 * author：daguozhu
 * date-time：2020/9/5 16:09
 * description：
 **/
interface RequestCallback {

    /** 请求网络开始前，UI线程 */
    fun onStart(call: Call)

    /** 对返回数据进行操作的回调， UI线程 */
    fun onHttpResponse(httpResponse: Response, result: String)

    /** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程 */
    fun onFailure(e: Exception, code: Int, msg: String?)

    /** 请求网络结束后，UI线程 */
    fun onFinish()
}

abstract class RequestCallbackImpl : RequestCallback {

    companion object {
        const val TAG = "RequestCallbackImpl"
    }

    override fun onStart(call: Call) {
        "onStart".logD(TAG)
    }

    override fun onHttpResponse(httpResponse: Response, result: String) {
        "onHttpResponse".logD(TAG)
    }

    override fun onFailure(e: Exception, code: Int, msg: String?) {
        "onFailure:$e".logE(TAG)
    }

    override fun onFinish() {
        "onFinish".logD(TAG)
    }
}