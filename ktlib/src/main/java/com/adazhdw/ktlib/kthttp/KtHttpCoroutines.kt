package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.RequestGsonCallback
import com.adazhdw.ktlib.kthttp.constant.GET
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.param.KParams
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 协程方法
 */

suspend inline fun <reified T : Any> requestCoroutines(
    method: Method = GET,
    url: String,
    params: KParams? = null
): T {
    return suspendCancellableCoroutine { continuation ->
        var call: Call? = null
        continuation.invokeOnCancellation {
            call?.cancel()
        }
        call = KtHttp.request(method, url, params, object : RequestGsonCallback<T>() {

            override fun onError(e: Exception, code: Int, msg: String?) {
                super.onError(e, code, msg)
                if (continuation.isCancelled) return
                continuation.resumeWithException(e)
            }

            override fun onError(code: Int, msg: String?) {

            }

            override fun onSuccess(data: T) {
                continuation.resume(data)
            }
        })
    }
}