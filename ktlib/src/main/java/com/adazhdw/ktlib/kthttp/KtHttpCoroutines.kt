package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.RequestGsonCallback
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: 网络请求协程类
 */

/**
 * get 网络请求协程方法
 */
suspend inline fun <reified T : Any> getCoroutines(
    url: String,
    params: Params
): T {
    return requestCoroutines(method = Method.GET, url, params)
}

/**
 * post 网络请求协程方法
 */
suspend inline fun <reified T : Any> postCoroutines(
    url: String,
    params: Params
): T {
    return requestCoroutines(method = Method.POST, url, params)
}

/**
 * 未分类 网络请求协程方法
 */
suspend inline fun <reified T : Any> requestCoroutines(
    method: Method = Method.GET,
    url: String,
    params: Params
): T {
    return suspendCancellableCoroutine { continuation ->
        var request: BaseRequest<*>? = null
        continuation.invokeOnCancellation {
            request?.cancel()
        }
        request = KtHttp.request(method, url, params, object : RequestGsonCallback<T>() {

            override fun onFailure(e: Exception, code: Int, msg: String?) {
                super.onFailure(e, code, msg)
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