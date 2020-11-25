package com.adazhdw.ktlib.kthttp.callback

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.ext.logE
import okhttp3.Call
import okhttp3.Response

/**
 * author：daguozhu
 * date-time：2020/11/25 17:10
 * description：
 **/

abstract class RequestCallbackImpl(private val lifecycleOwner: LifecycleOwner?) : RequestCallback {

    companion object {
        const val TAG = "RequestCallbackImpl"
    }

    override val mLifecycleOwner: LifecycleOwner?
        get() = lifecycleOwner

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