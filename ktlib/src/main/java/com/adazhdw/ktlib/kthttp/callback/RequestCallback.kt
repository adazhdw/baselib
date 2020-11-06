package com.adazhdw.ktlib.kthttp.callback

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
    fun onHttpResponse(httpResponse: Response, result: String?)

    /** 对返回数据进行操作的回调， UI线程 */
    fun onResponse(result: String)

    /** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程 */
    fun onFailure(e: Exception, code: Int, msg: String?)

    /** 请求网络结束后，UI线程 */
    fun onFinish()
}

abstract class RequestCallbackImpl : RequestCallback {

    override fun onStart(call: Call) {

    }

    override fun onHttpResponse(httpResponse: Response, result: String?) {

    }

    override fun onResponse(result: String) {

    }

    override fun onFailure(e: Exception, code: Int, msg: String?) {

    }

    override fun onFinish() {

    }
}