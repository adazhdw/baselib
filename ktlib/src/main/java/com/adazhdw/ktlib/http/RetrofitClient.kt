package com.adazhdw.ktlib.http

import com.adazhdw.ktlib.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Retrofit单例类
 */
object RetrofitClient {
    //声明Retrofit对象
    private val mRetrofit: Retrofit
    private var mBaseUrl = ""

    inline fun <reified T> create(): T {
        return create(T::class.java)
    }

    fun <T> create(clazz: Class<T>): T {
        return mRetrofit.create(clazz)
    }

    fun initBaseUrl(baseUrl: String) {
        mBaseUrl = baseUrl
    }

    init {
        if (mBaseUrl.isBlank()) {
            error("base url is null ")
        }
        mRetrofit = Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .client(getClient())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(
                        KotlinJsonAdapterFactory()
                    ).build()
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(OkHttpLogger()).apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
        }
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(getLoggingInterceptor())
            .build()
    }
}