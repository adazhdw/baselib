package com.adazhdw.ktlib.http

import com.adazhdw.ktlib.BuildConfig
import com.adazhdw.ktlib.kthttp.util.OkHttpLogger
import com.adazhdw.ktlib.mBaseUrl
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Retrofit单例类
 */
object RetrofitClient {
    //声明Retrofit对象
    private val mRetrofit: Retrofit

    inline fun <reified T> create(): T {
        return create(T::class.java)
    }

    fun <T> create(clazz: Class<T>): T {
        return mRetrofit.create(clazz)
    }

    init {
        if (mBaseUrl.isBlank()) {
            error("base url is null ")
        }
        mRetrofit = Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(OkHttpLogger()).apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15L, TimeUnit.SECONDS)
            .callTimeout(15L, TimeUnit.SECONDS)
            .writeTimeout(15L, TimeUnit.SECONDS)
            .addNetworkInterceptor(getLoggingInterceptor())
            .build()
    }
}
