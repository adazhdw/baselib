package com.adazhdw.ktlib.hihttp

import android.os.Handler
import android.os.Looper
import com.adazhdw.ktlib.hihttp.callback.GsonHttpCallback
import com.adazhdw.ktlib.hihttp.callback.JsonHttpCallback
import com.adazhdw.ktlib.hihttp.callback.OkHttpCallback
import com.adazhdw.ktlib.hihttp.callback.RawHttpCallback
import com.adazhdw.ktlib.http.HttpConstant
import com.adazhdw.ktlib.http.OkHttpLogger
import com.adazhdw.ktlib.isDebug
import com.adazhdw.ktlib.utils.SSLUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

val http by lazy { HiHttp.mHiHttp }
val gson: Gson by lazy { GsonBuilder().create() }

class HiHttp private constructor() {

    companion object {
        val mHiHttp: HiHttp by lazy { HiHttp() }
    }

    private val handler: Handler
    private val okHttpClient: OkHttpClient
    private val mHandler = Handler(Looper.getMainLooper())

    init {
        okHttpClient = getClient()
        handler = Handler(Looper.getMainLooper())
    }

    fun get(params: Params, callback: OkHttpCallback) {
        if (params.url.isBlank()) return
        val request = requestBuilder(params).url(params.url).get().build()
        request(request, callback)
    }

    fun post(params: Params, callback: OkHttpCallback) {
        if (params.url.isBlank()) return
        val request = requestBuilder(params).url(params.url).post(params.postRequestBody()).build()
        request(request, callback)
    }

    fun postFile(params: Params, fileKey: String, file: File, callback: OkHttpCallback) {
        if (params.url.isBlank() || !file.exists()) return
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for ((key, value) in params.params) {
            builder.addFormDataPart(key, value)
        }
        builder.addFormDataPart(fileKey, file.name, file.asRequestBody(ContentType.getType(file).toMediaTypeOrNull()))
        val request = requestBuilder(params).url(params.url).post(builder.build()).build()
        request(request, callback)
    }

    fun postFiles(params: Params, fileKey: String, files: List<File>, callback: OkHttpCallback) {
        if (params.url.isBlank() || files.isEmpty()) return
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for ((key, value) in params.params) {
            builder.addFormDataPart(key, value)
        }
        for (file in files) {
            builder.addFormDataPart(fileKey, file.name, file.asRequestBody(ContentType.getType(file).toMediaTypeOrNull()))
        }
        val request = requestBuilder(params).url(params.url).post(builder.build()).build()
        request(request, callback)
    }

    private fun request(request: Request, callback: OkHttpCallback) {
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mHandler.post { callback.onError(e) }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body
                if (body != null) {
                    try {
                        val result = String(body.bytes())
                        when (callback) {
                            is RawHttpCallback -> mHandler.post { callback.onSuccess(result) }
                            is JsonHttpCallback -> mHandler.post { callback.onSuccess(JSONObject(result)) }
                            is GsonHttpCallback<*> -> mHandler.post { callback.onSuccess(gson.fromJson(result, callback.mType)) }
                        }
                    } catch (e: Exception) {
                        mHandler.post { callback.onError(e) }
                    }
                } else {
                    val e = RuntimeException("body of response is null:${call.request().url}")
                    mHandler.post { callback.onError(e) }
                }
            }
        })
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
