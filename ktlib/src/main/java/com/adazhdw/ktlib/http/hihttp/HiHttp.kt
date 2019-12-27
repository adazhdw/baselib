package com.adazhdw.ktlib.http.hihttp

import android.os.Handler
import android.os.Looper
import com.adazhdw.ktlib.http.HttpConstant
import com.adazhdw.ktlib.http.OkHttpLogger
import com.adazhdw.ktlib.isDebug
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
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
                        mHandler.post { callback.onSuccess(result) }
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
                builder.addHeader(key, value.toString())
        }
        return builder
    }

    private fun getClient(): OkHttpClient {
        val (key, value) = SSLUtils.initSSLSocketFactory()
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

val CONTENT_TYPE_JSON = ("application/json; charset=utf-8").toMediaTypeOrNull()
val CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=utf-8".toMediaTypeOrNull()
val CONTENT_TYPE_FILE = "application/octet-stream".toMediaTypeOrNull()

class Params {

    companion object {
        fun jsonParam(url: String): Params = Params(url, CONTENT_TYPE_JSON, false)
    }

    val params: MutableMap<String, Any> = mutableMapOf()
    val needHeaders: Boolean
    val url: String
    private val jsonParams: MutableMap<String, Any> = mutableMapOf()
    private val contentType: MediaType?

    constructor(
        url: String,
        contentType: MediaType? = CONTENT_TYPE_FORM,
        needHeaders: Boolean = false
    ) {
        this.url = url
        this.contentType = contentType
        this.needHeaders = needHeaders
    }

    fun put(key: String, value: Any) {
        if (isFormContent()) {
            params[key] = value
        } else {
            jsonParams[key] = value
        }
    }

    fun put(map: Map<String, Any>) {
        for ((key, value) in map) {
            if (isFormContent()) {
                params[key] = value
            } else {
                jsonParams[key] = value
            }
        }
    }

    fun postRequestBody(): RequestBody {
        return if (isFormContent()) {
            val builder = FormBody.Builder()
            for ((key, value) in params) {
                builder.add(key, value.toString())
            }
            builder.build()
        } else {
            gson.toJson(jsonParams).toRequestBody(CONTENT_TYPE_JSON)
        }
    }

    private fun isFormContent(): Boolean {
        return contentType == CONTENT_TYPE_FORM
    }
}