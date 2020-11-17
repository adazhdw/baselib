package com.adazhdw.ktlib.kthttp.ext

import com.adazhdw.ktlib.kthttp.KtHttp.Companion.ktHttp
import com.adazhdw.ktlib.kthttp.callback.RequestJsonCallback
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.ktlib.kthttp.request.RequestCall
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
    param: Param = Param.build()
): T {
    return requestCoroutines(method = Method.GET, url, param)
}

/**
 * post 网络请求协程方法
 */
suspend inline fun <reified T : Any> postCoroutines(
    url: String,
    param: Param = Param.build()
): T {
    return requestCoroutines(method = Method.POST, url, param)
}

/**
 * 未分类 网络请求协程方法
 */
suspend inline fun <reified T : Any> requestCoroutines(
    method: Method = Method.GET,
    url: String,
    param: Param = Param.build()
): T {
    return suspendCancellableCoroutine { continuation ->
        var request: RequestCall? = null
        continuation.invokeOnCancellation {
            request?.cancel()
        }
        request = ktHttp.request(method, url, param, object : RequestJsonCallback<T>() {

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