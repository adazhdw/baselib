package com.adazhdw.kthttp.callback

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.ext.logE
import okhttp3.Call
import okhttp3.ResponseBody

/**
 * author：daguozhu
 * date-time：2020/12/1 14:58
 * description：
 **/
open class RequestCallbackImpl(private val owner: LifecycleOwner?) : RequestCallback {
    final override val mLifecycleOwner: LifecycleOwner?
        get() = owner

    companion object {
        const val TAG = "RequestCallbackImpl"
    }

    override fun onStart(call: Call) {
        "onStart".logD(TAG)
    }

    override fun onResult(body: ResponseBody, result: String) {
        "onHttpResponse".logD(TAG)
    }

    override fun onFailure(e: Exception, code: Int, msg: String?) {
        "onFailure:$e".logE(TAG)
    }

    override fun onFinish() {
        "onFinish".logD(TAG)
    }

}