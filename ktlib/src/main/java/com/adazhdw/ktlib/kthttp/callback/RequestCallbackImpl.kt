package com.adazhdw.ktlib.kthttp.callback

import okhttp3.Call
import okhttp3.Response

/**
 * author：daguozhu
 * date-time：2020/9/5 17:48
 * description：
 **/
abstract class RequestCallbackImpl : RequestCallback {

    override fun onStart(call: Call) {

    }

    override fun onResponse(httpResponse: Response, response: String?) {

    }

    override fun onResponse(result: String) {

    }

    override fun onFailure(e: Exception, code: Int, msg: String?) {

    }

    override fun onFinish() {

    }
}