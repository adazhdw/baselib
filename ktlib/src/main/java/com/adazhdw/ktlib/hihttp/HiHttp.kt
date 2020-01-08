package com.adazhdw.ktlib.hihttp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import com.adazhdw.ktlib.core.mainThread
import com.adazhdw.ktlib.core.networkIO
import com.adazhdw.ktlib.hihttp.callback.RawHttpCallback
import com.adazhdw.ktlib.http.HttpConstant
import com.adazhdw.ktlib.http.OkHttpLogger
import com.adazhdw.ktlib.isDebug
import com.adazhdw.ktlib.utils.SSLUtil
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

class HiHttp private constructor() {

    companion object {
        val mHiHttp: HiHttp by lazy { HiHttp() }
    }

    private val okHttpClient: OkHttpClient

    init {
        okHttpClient = getClient()
    }

    fun get(params: Params, rawHttpCallback: RawHttpCallback, context: Context?) {
        if (params.url.isBlank()) return
        val request = requestBuilder(params).url(params.url).get().build()
        request(request, rawHttpCallback, context)
    }

    fun post(params: Params, rawHttpCallback: RawHttpCallback, context: Context?) {
        if (params.url.isBlank()) return
        val request = requestBuilder(params).url(params.url).post(params.postRequestBody()).build()
        request(request, rawHttpCallback, context)
    }

    fun postFile(
        params: Params,
        fileKey: String,
        file: File,
        rawHttpCallback: RawHttpCallback,
        context: Context?
    ) {
        if (params.url.isBlank() || !file.exists()) return
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for ((key, value) in params.params) {
            builder.addFormDataPart(key, value)
        }
        builder.addFormDataPart(
            fileKey,
            file.name,
            file.asRequestBody(ContentType.getType(file).toMediaTypeOrNull())
        )
        val request = requestBuilder(params).url(params.url).post(builder.build()).build()
        request(request, rawHttpCallback, context)
    }

    fun postFiles(
        params: Params,
        fileKey: String,
        files: List<File>,
        rawHttpCallback: RawHttpCallback,
        context: Context?
    ) {
        if (params.url.isBlank() || files.isEmpty()) return
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for ((key, value) in params.params) {
            builder.addFormDataPart(key, value)
        }
        for (file in files) {
            builder.addFormDataPart(
                fileKey,
                file.name,
                file.asRequestBody(ContentType.getType(file).toMediaTypeOrNull())
            )
        }
        val request = requestBuilder(params).url(params.url).post(builder.build()).build()
        request(request, rawHttpCallback, context)
    }

    private fun request(request: Request, callback: RawHttpCallback, context: Context?) {
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainThread.execute {
                    if (contextActive(context)) callback.onException(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                networkIO.submit {
                    val body = response.body
                    if (body != null) {
                        try {
                            val result = String(body.bytes())
                            mainThread.execute {
                                if (contextActive(context)) callback.onSuccess(result)
                            }
                        } catch (e: Exception) {
                            mainThread.execute {
                                if (contextActive(context)) callback.onException(e)
                            }
                        }
                    } else {
                        val e = RuntimeException("body of response is null:${call.request().url}")
                        mainThread.execute {
                            if (contextActive(context)) callback.onException(e)
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun contextActive(context: Context?): Boolean {
        if (context == null) return false
        if (context is Activity) {
            if (context.isFinishing) return false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context.isDestroyed) return false
        }
        return true
    }

    private fun requestBuilder(params: Params): Request.Builder {
        val builder = Request.Builder()
        if (params.needHeaders) {
            for ((key, value) in params.params)
                builder.addHeader(key, value)
        }
        return builder
    }

    private fun getClient(): OkHttpClient {
        val (key, value) = SSLUtil.initSSLSocketFactory()
        return OkHttpClient.Builder()
            .connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(getLoggingInterceptor())
            .hostnameVerifier(HostnameVerifier { _, _ -> true })/*添加https支持*/
            .sslSocketFactory(key, value)/*添加SSL证书信任*/
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(OkHttpLogger()).apply {
            level = if (isDebug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
    }
}
