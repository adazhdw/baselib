package com.adazhdw.ktlib.hihttp

import com.adazhdw.ktlib.hihttp.callback.FastJsonHttpCallback
import com.adazhdw.ktlib.hihttp.callback.RawHttpCallback
import com.alibaba.fastjson.JSONObject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by adazhdw on 2020/1/3.
 */

class HiHttpCoroutines
val mHiHttp by lazy { HiHttp.mHiHttp }

inline fun <reified T : Any> parseObject(json: String): T {
    return JSONObject.parseObject(json, T::class.javaObjectType)
}

inline fun <reified T : Any> get(params: Params, fastJsonHttpCallback: FastJsonHttpCallback<T>) {
    mHiHttp.get(params, object : RawHttpCallback() {
        override fun onSuccess(data: String) {
            fastJsonHttpCallback.onSuccess(parseObject(data))
        }

        override fun onException(e: Exception) {
            fastJsonHttpCallback.onException(e)
        }
    })
}

suspend inline fun <reified T : Any> getCoroutine(
    params: Params,
    noinline onResponse: ((data: T) -> Unit)? = null,
    noinline onError: ((e: Exception) -> Unit)? = null
): T {
    return suspendCancellableCoroutine { continuation ->
        get(params, object : FastJsonHttpCallback<T>() {
            override fun onSuccess(data: T) {
                onResponse?.invoke(data)
                continuation.resume(data)
            }

            override fun onException(e: Exception) {
                //错误处理
                onError?.invoke(e)
            }
        })
    }
}