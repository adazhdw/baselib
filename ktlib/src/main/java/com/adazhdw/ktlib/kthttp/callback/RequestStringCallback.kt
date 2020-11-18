package com.adazhdw.ktlib.kthttp.callback

import androidx.lifecycle.LifecycleOwner
import okhttp3.Response

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: Gson回调转换泛型类 T
 */
abstract class RequestStringCallback(mLifecycleOwner: LifecycleOwner?) :
    RequestCallbackImpl(mLifecycleOwner) {

    override fun onHttpResponse(httpResponse: Response, result: String) {
        super.onHttpResponse(httpResponse, result)
        onSuccess(result)
    }

    abstract fun onSuccess(response: String)

    abstract fun onError(code: Int, msg: String?)

    override fun onFailure(e: Exception, code: Int, msg: String?) {
        super.onFailure(e, code, msg)
        onError(code, msg)
    }
}