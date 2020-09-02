package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.RequestGsonCallback
import com.adazhdw.ktlib.kthttp.constant.GET
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.constant.POST
import com.adazhdw.ktlib.kthttp.param.Param
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
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
    param: Param? = null
): T {
    return requestCoroutines(method = GET, url, param)
}

/**
 * post 网络请求协程方法
 */
suspend inline fun <reified T : Any> postCoroutines(
    url: String,
    param: Param? = null
): T {
    return requestCoroutines(method = POST, url, param)
}

/**
 * 未分类 网络请求协程方法
 */
suspend inline fun <reified T : Any> requestCoroutines(
    method: Method = GET,
    url: String,
    param: Param? = null
): T {
    return suspendCancellableCoroutine { continuation ->
        var call: Call? = null
        continuation.invokeOnCancellation {
            call?.cancel()
        }
        call = KtHttp.request(method, url, param, object : RequestGsonCallback<T>() {

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