package com.adazhdw.ktlib.hihttp

import com.adazhdw.ktlib.hihttp.callback.GsonHttpCallback
import com.adazhdw.ktlib.hihttp.callback.RawHttpCallback
import com.alibaba.fastjson.JSONObject
import com.google.gson.GsonBuilder
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by adazhdw on 2020/1/3.
 */

class HiHttpCoroutines

val mHiHttp by lazy { HiHttp.mHiHttp }
val gson by lazy { GsonBuilder().create() }

inline fun <reified T : Any> get(params: Params, callback: GsonHttpCallback<T>) {
    mHiHttp.get(params, object : RawHttpCallback() {
        override fun onSuccess(data: String) {
            try {
                callback.onSuccess(gson.fromJson(data,callback.mType))
            }catch (e:Exception){
                callback.onException(e)
            }
        }

        override fun onException(e: Exception) {
            callback.onException(e)
        }
    })
}

suspend inline fun <reified T : Any> getCoroutine(
    params: Params,
    noinline onResponse: ((data: T) -> Unit)? = null,
    noinline onError: ((e: Exception) -> Unit)? = null
): T {
    return suspendCancellableCoroutine { continuation ->
        get(params, object : GsonHttpCallback<T>() {
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

inline fun <reified T : Any> post(params: Params,  callback: GsonHttpCallback<T>) {
    mHiHttp.post(params, object : RawHttpCallback() {
        override fun onSuccess(data: String) {
            callback.onSuccess(gson.fromJson(data,callback.mType))
        }

        override fun onException(e: Exception) {
            callback.onException(e)
        }
    })
}

suspend inline fun <reified T : Any> postCoroutine(
    params: Params,
    noinline onResponse: ((data: T) -> Unit)? = null,
    noinline onError: ((e: Exception) -> Unit)? = null
): T {
    return suspendCancellableCoroutine { continuation ->
        post(params, object : GsonHttpCallback<T>() {
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