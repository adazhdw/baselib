package com.adazhdw.ktlib.kthttp.callback

import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.kthttp.exception.ExceptionHelper
import com.adazhdw.ktlib.kthttp.exception.NetException
import com.adazhdw.ktlib.kthttp.request.CallProxy
import com.adazhdw.ktlib.kthttp.util.HttpLifecycleObserver
import okhttp3.Call
import okhttp3.Response

/**
 * author：daguozhu
 * date-time：2020/11/18 9:30
 * description：
 **/

open class OkHttpCallbackImpl constructor(
    callProxy: CallProxy,
    private val requestCallback: RequestCallback?
) : OkHttpCallback(callProxy, requestCallback?.mLifecycleOwner) {
    init {
        KtExecutors.mainThread.execute {
            if (isLifecycleActive()) {
                requestCallback?.onStart(mCallProxy.call)
            }
        }
    }

    override fun response(response: Response) {
        val body = ExceptionHelper.getNotNullResponseBody(response)
        val result = body.string()
        if (isLifecycleActive() && requestCallback != null) {
            requestCallback.onResult(body, result)
        }
    }

    override fun failure(e: Exception, call: Call) {
        e.printStackTrace()
        val ex: NetException = ExceptionHelper.callError(e)
        if (isLifecycleActive() && requestCallback != null) {
            KtExecutors.mainThread.execute {
                requestCallback.onFailure(e, ex.code, ex.msg)
                requestCallback.onFinish()
            }
        }
    }

    /**
     * 判断当前宿主是否处于活动状态
     */
    private fun isLifecycleActive() = HttpLifecycleObserver.isLifecycleActive(mLifecycleOwner)

}