package com.adazhdw.ktlib.http


import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.*
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
        continuation.invokeOnCancellation {
            cancel()
        }

        this.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        val invocation = call.request().tag(Invocation::class.java)!!
                        val method = invocation.method()
                        val errorMsg =
                            "Response from ${method.declaringClass.name}.${method.name} was null but response body type was declared as non-null"
                        val e = KotlinNullPointerException(errorMsg)
                        continuation.resumeWithException(e)
                    }
                } else {
                    continuation.resumeWithException(HttpException(response))
                }
            }
        })
    }
}

@JvmName("awaitNullable")
suspend fun <T : Any> Call<T?>.awaitNull(): T? {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            cancel()
        }
        enqueue(object : Callback<T?> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body())
                } else {
                    continuation.resumeWithException(HttpException(response))
                }
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }
}

suspend fun <T : Any> Call<T>.awaitResponse(): Response<T> {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            cancel()
        }
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                continuation.resume(response)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }
}
