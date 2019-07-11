package com.adazhdw.baselibrary.http

import com.adazhdw.baselibrary.ext.logD
import com.adazhdw.baselibrary.ext.logE
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * RxJava常用实践
 * 1、有一些操作不想写在业务层，而是想做一个统一的处理。
 * 更直白的说法是 : 我的统一操作不依赖于特定的数据类型，而只需要一些共有的参数
 * 这时候可以使用doOnNext()
 */
//Retrofit.Call
suspend fun <T> Call<T>.await(): T {
    return suspendCancellableCoroutine { continuation ->
        this.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
                logE("onError---${t.message}")
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body != null) continuation.resume(body)
                else continuation.resumeWithException(RuntimeException("response body is null"))
                logD("onSuccess")
            }
        })
    }
}
