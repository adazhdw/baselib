package com.adazhdw.kthttp

import com.adazhdw.kthttp.coder.ICoder
import com.adazhdw.kthttp.coder.UrlCoder
import com.adazhdw.kthttp.constant.HttpConstant
import com.adazhdw.kthttp.converter.GsonConverter
import com.adazhdw.kthttp.converter.IConverter
import com.adazhdw.kthttp.interceptor.RetryInterceptor
import com.adazhdw.kthttp.ssl.HttpsUtils
import com.adazhdw.kthttp.util.OkHttpLogger
import com.adazhdw.kthttp.util.logging.Level
import com.adazhdw.kthttp.util.logging.LoggingInterceptor
import com.adazhdw.ktlib.KtLib
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * author：daguozhu
 * date-time：2020/11/16 15:05
 * description：
 **/
object KtConfig {

    var coder: ICoder = UrlCoder.create()
    var converter: IConverter = GsonConverter.create()
    var needDecodeResult = false
    var mOkHttpClient = getOkHttpClient()

    @JvmOverloads
    fun getOkHttpClient(timeout: Long = HttpConstant.DEFAULT_TIMEOUT): OkHttpClient {
        val sslParams = HttpsUtils.getSslSocketFactory()
        return OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .callTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor2())
            .addInterceptor(RetryInterceptor())
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).build()
    }

    fun getLoggingInterceptor2(): Interceptor {
        val interceptor = HttpLoggingInterceptor(OkHttpLogger())
        if (KtLib.isDebug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
        return interceptor
    }

    fun getLoggingInterceptor(): Interceptor {
        val level: Level = if (KtLib.isDebug) Level.BODY else Level.BASIC
        return LoggingInterceptor.Builder()
            .setLevel(level).build()
    }

}