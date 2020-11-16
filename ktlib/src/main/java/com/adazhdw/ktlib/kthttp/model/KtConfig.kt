package com.adazhdw.ktlib.kthttp.model

import com.adazhdw.ktlib.KtLib.isDebug
import com.adazhdw.ktlib.kthttp.coder.ICoder
import com.adazhdw.ktlib.kthttp.coder.UrlCoder
import com.adazhdw.ktlib.kthttp.converter.GsonConverter
import com.adazhdw.ktlib.kthttp.converter.IConverter
import com.adazhdw.ktlib.kthttp.interceptor.RetryInterceptor
import com.adazhdw.ktlib.kthttp.ssl.HttpsUtils
import com.adazhdw.ktlib.kthttp.util.OkHttpLogger
import com.adazhdw.ktlib.kthttp.util.logging.Level
import com.adazhdw.ktlib.kthttp.util.logging.LoggingInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * author：daguozhu
 * date-time：2020/11/16 15:05
 * description：
 **/
class KtConfig private constructor() {
    companion object {
        val ktConfig: KtConfig by lazy { KtConfig() }
    }

    private var coder: ICoder = UrlCoder.create()
    private var converter: IConverter = GsonConverter.create()
    private var needDecodeResult = false
    private var mOkHttpClient = getOkHttpClient(HttpConstant.DEFAULT_TIMEOUT)
    private var debug = true

    fun setCoder(coder: ICoder): KtConfig {
        this.coder = coder
        return this
    }

    fun coder() = coder

    fun setConverter(iConverter: IConverter): KtConfig {
        this.converter = iConverter
        return this
    }

    fun converter() = converter

    fun setNeedDecodeResult(needDecodeResult: Boolean): KtConfig {
        this.needDecodeResult = needDecodeResult
        return this
    }

    fun needDecodeResult() = needDecodeResult

    fun setDebug(debug: Boolean): KtConfig {
        this.debug = debug
        return this
    }

    fun debug() = debug


    fun getOkHttpClient(): OkHttpClient {
        return this.mOkHttpClient
    }

    fun setOkHttpClient(mOkHttpClient: OkHttpClient): KtConfig {
        this.mOkHttpClient = mOkHttpClient
        return this
    }

    fun getOkHttpClient(timeout: Long): OkHttpClient {
        val sslParams = HttpsUtils.getSslSocketFactory()
        return OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .callTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor2())
            .addInterceptor(RetryInterceptor())
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).build()
    }

    private fun getLoggingInterceptor2(): Interceptor {
        val interceptor = HttpLoggingInterceptor(OkHttpLogger())
        if (isDebug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
        return interceptor
    }

    fun getLoggingInterceptor(): Interceptor {
        val level: Level = if (isDebug) Level.BODY else Level.BASIC
        return LoggingInterceptor.Builder()
            .setLevel(level).build()
    }

}