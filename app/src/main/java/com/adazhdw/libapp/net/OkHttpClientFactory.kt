package com.adazhdw.libapp.net

import android.content.Context
import android.util.Log
import com.adazhdw.kthttp.util.NetworkUtils
import com.adazhdw.net.HttpHeaders
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class OkHttpClientFactory(private val applicationContext: Context) {


    fun create(): okhttp3.OkHttpClient {
        //设置缓存路径，内部存储
        //val httpCacheDirectory = File(context.cacheDir,"responses")
        //外部存储
        val httpCacheDirectory = File(applicationContext.cacheDir, "responses")
        //设置缓存 10M
        val cacheSize: Long = 10 * 1021 * 1024
        val cache = Cache(httpCacheDirectory, cacheSize)

        return okhttp3.OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(GetCookiesInterceptor())
            .addInterceptor(GetCache())
            .addNetworkInterceptor(WriteCache())
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private val cookies = HashSet<String>()

    inner class GetCookiesInterceptor : Interceptor {

        private var headers: List<String>? = null

        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            headers = originalResponse.headers(HttpHeaders.KEY_SET_COOKIE)
            headers?.let { headers ->
                for (header in headers) {
                    cookies.add(header)
                }
            }
            return originalResponse
        }

    }

    inner class AddCookiesInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            for (cookie in cookies) {
                builder.addHeader(HttpHeaders.KEY_COOKIE, cookie)
                Log.v("OkHttpClientFactory", "AddCookiesInterceptor:Adding Header:$cookie")
            }
            return chain.proceed(builder.build())
        }

    }

    //获取缓存
    inner class GetCache : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            if (!NetworkUtils.isConnected(applicationContext)) {
                val maxStale: Int = 60 * 60 * 24 * 28//离线缓存保存4周，单位秒
                val tempCacheControl = CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(maxStale, TimeUnit.SECONDS)
                    .build()
                request = request.newBuilder()
                    .cacheControl(tempCacheControl)
                    .build()
                Log.i("OkHttpClientFactory", "GetCache:intercept:no network ")
            }
            return chain.proceed(request)
        }

    }

    inner class WriteCache : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val originalResponse = chain.proceed(request)
            val maxAge: Int = 1 * 60 // 在线缓存在1分钟内可读取 单位:秒
            return originalResponse.newBuilder()
                .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        }

    }
}