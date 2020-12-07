package com.adazhdw.ktlib.kthttp.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * author：daguozhu
 * date-time：2020/9/2 13:37
 * description：
 **/
class RetryInterceptor(private val maxCount: Int = 3, private val sleepMillis: Long = 2000L) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response?
        var tryCount = 0
        val request = chain.request()
        response = sendRequest(chain, request)
        while (response == null && tryCount < maxCount) {
            tryCount++
            try {
                Thread.sleep(sleepMillis)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            response = sendRequest(chain, request)
        }
        // If there was no internet connection, then response will be null.
        // Need to initialize response anyway to avoid NullPointerException.
        try {
            if (response == null) response = chain.proceed(request)
            return response
        } catch (e: Exception) {
            throw e
        }
    }

    private fun sendRequest(chain: Interceptor.Chain, request: Request): Response? {
        return try {
            chain.proceed(request)
        } catch (e: IOException) {
            null
        }
    }
}