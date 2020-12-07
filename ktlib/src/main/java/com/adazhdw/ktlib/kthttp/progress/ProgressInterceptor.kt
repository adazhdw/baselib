package com.adazhdw.ktlib.kthttp.progress

import com.adazhdw.ktlib.kthttp.progress.body.ProgressResponseBody
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 下载进度拦截器
 * User: ljx
 * Date: 2019/1/20
 * Time: 14:19
 */
class ProgressInterceptor(private val progressCallback: ProgressCallback) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //拦截
        val originalResponse = chain.proceed(chain.request())
        //包装响应体并返回
        return originalResponse.newBuilder()
            .body(ProgressResponseBody(originalResponse, progressCallback))
            .build()
    }
}