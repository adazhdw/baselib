package com.adazhdw.baselibrary.http

import com.adazhdw.baselibrary.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


val retrofitModel by lazy { RetrofitModel.retrofitModel }

fun <T> apiService(service: Class<T>): T {
    return retrofitModel.mRetrofit.create(service)
}

/**
 * Retrofit单例类
 */
class RetrofitModel {
    //声明Retrofit对象
    val mRetrofit: Retrofit

    init {
        if (mBaseUrl.isBlank()) {
            error("base url is null ")
        }
        mRetrofit = Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .client(getClient())
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(OkHttpLogger()).apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
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

    companion object {
        private var mBaseUrl = ""
        val retrofitModel: RetrofitModel by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { RetrofitModel() }
        /*//由于该对象会被频繁调用，采用单例模式，下面是一种线程安全模式的单例写法
        @Volatile
        private var INSTANCE: RetrofitModel? = null

        fun getINSTANCE(): RetrofitModel =
            INSTANCE ?: synchronized(RetrofitModel::class.java) {
                INSTANCE ?: RetrofitModel().also { INSTANCE = it }
            }*/
        fun initBaseUrl(baseUrl: String) {
            mBaseUrl = baseUrl
        }
    }
}
