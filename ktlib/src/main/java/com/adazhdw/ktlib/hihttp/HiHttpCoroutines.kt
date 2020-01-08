package com.adazhdw.ktlib.hihttp

import android.content.Context
import com.adazhdw.ktlib.hihttp.callback.FastJsonHttpCallback
import com.adazhdw.ktlib.hihttp.callback.GsonHttpCallback
import com.adazhdw.ktlib.hihttp.callback.RawHttpCallback
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.google.gson.GsonBuilder
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by adazhdw on 2020/1/3.
 */

class HiHttpCoroutines

val mHiHttp by lazy { HiHttp.mHiHttp }
val gson by lazy { GsonBuilder().create() }

inline fun <reified T : Any> Context?.get(params: Params, callback: FastJsonHttpCallback<T>) {
    mHiHttp.get(params, object : RawHttpCallback() {
        override fun onSuccess(data: String) {
            try {
//                callback.onSuccess(gson.fromJson(data,callback.mType))
                callback.onSuccess(JSONObject.parseObject(data, object : TypeReference<T>() {}))
            }catch (e:Exception){
                callback.onException(e)
            }
        }

        override fun onException(e: Exception) {
            callback.onException(e)
        }
    },this)
}

suspend inline fun <reified T : Any> Context?.getCoroutine(
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

inline fun <reified T : Any> Context?.post(params: Params,  callback: FastJsonHttpCallback<T>) {
    mHiHttp.post(params, object : RawHttpCallback() {
        override fun onSuccess(data: String) {
            callback.onSuccess(JSONObject.parseObject(data, object : TypeReference<T>() {}))
        }

        override fun onException(e: Exception) {
            callback.onException(e)
        }
    },this)
}

suspend inline fun <reified T : Any> Context?.postCoroutine(
    params: Params,
    noinline onResponse: ((data: T) -> Unit)? = null,
    noinline onError: ((e: Exception) -> Unit)? = null
): T {
    return suspendCancellableCoroutine { continuation ->
        post(params, object : FastJsonHttpCallback<T>() {
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