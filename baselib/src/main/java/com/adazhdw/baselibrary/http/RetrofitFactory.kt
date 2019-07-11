package com.adazhdw.baselibrary.http

import com.adazhdw.baselibrary.LibUtil
import com.adazhdw.baselibrary.isDebug
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Retrofit 工厂类
 */
abstract class RetrofitFactory<T> {
    private var mRetrofit: Retrofit? = null
    private var mBaseUrl = ""
    var apiService: T

    abstract fun getService(): Class<T>
    abstract fun baseUrl(): String

    init {
        mBaseUrl = this.baseUrl()
        if (mBaseUrl.isEmpty()) {
            throw BaseUrlNullException()
        }
        apiService = this.getRetrofit().create(this.getService())
    }

    private fun getRetrofit(): Retrofit {
        mRetrofit ?: synchronized(RetrofitFactory::class.java) {
            mRetrofit ?: Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().also { mRetrofit = it }
        }
        return mRetrofit!!
    }

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        val cacheFile = File(LibUtil.getApp().cacheDir, "cache")
        val cache = Cache(cacheFile, HttpConstant.MAX_CACHE_SIZE)
        okHttpClient.run {
            addInterceptor(HttpLoggingInterceptor(OkHttpLogger()).apply {
                level = if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
            })
            cache(cache)
            connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            callTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }
        return okHttpClient.build()
    }
}

class BaseUrlNullException : Exception("base url is null ")


