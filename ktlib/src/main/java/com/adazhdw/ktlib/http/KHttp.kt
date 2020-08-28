package com.adazhdw.ktlib.http

import android.os.Handler
import android.os.Looper
import com.adazhdw.ktlib.BuildConfig
import com.adazhdw.ktlib.kthttp.param.KParams
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Author: dgz
 * Date: 2020/8/21 14:43
 * Description: http请求类
 */
val khttp = KHttp.instance

class KHttp private constructor() {

    companion object {
        val instance: KHttp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { KHttp() }
        val JSON = "application/json; charset=utf-8".toMediaType()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(getLoggingInterceptor())
            .build()
    }
    private val mHandler = Handler(Looper.getMainLooper())
    val gson: Gson by lazy { GsonBuilder().create() }

    inline fun <reified T> get(
        url: String,
        param: KParams? = null,
        crossinline onSuccess: (data: T) -> Unit,
        crossinline onFail: (e: Exception) -> Unit = {}
    ) {
        get(url, param, object : RequestCallback {
            override fun onSuccess(result: String) {
                try {
                    val data = gson.fromJson<T>(result, object : TypeToken<T>() {}.type)
                    onSuccess.invoke(data)
                } catch (e: JsonParseException) {
                    onError(e)
                } catch (e: Exception) {
                    onError(e)
                }
            }

            override fun onError(e: Exception) {
                onFail.invoke(e)
            }
        })
    }

    inline fun <reified T> post(
        url: String,
        param: KParams,
        crossinline onSuccess: (data: T) -> Unit,
        crossinline onFail: (e: Exception) -> Unit = {}
    ) {
        post(url, param, object : RequestCallback {
            override fun onSuccess(result: String) {
                try {
                    val data = gson.fromJson<T>(result, object : TypeToken<T>() {}.type)
                    onSuccess.invoke(data)
                } catch (e: JsonParseException) {
                    onError(e)
                } catch (e: Exception) {
                    onError(e)
                }
            }

            override fun onError(e: Exception) {
                onFail.invoke(e)
            }
        })
    }

    fun get(url: String, param: KParams? = null, callback: RequestCallback? = null) {
        val requestUrl = obtainGetUrl(url, param)
        val request = Request.Builder().url(requestUrl).get().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                handleError(e, callback)
            }

            override fun onResponse(call: Call, response: Response) {
                handleSuccess(response, callback)
            }
        })
    }

    fun post(url: String, param: KParams, callback: RequestCallback? = null) {
        val requestBody = param.getRequestBody()
        val requestBuilder = Request.Builder().url(url).post(requestBody)
        builderParams(requestBuilder, param)
        val request = requestBuilder.build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                handleError(e, callback)
            }

            override fun onResponse(call: Call, response: Response) {
                handleSuccess(response, callback)
            }
        })
    }

    fun cancel(tag: String?) {
        if (tag.isNullOrBlank()) return
        okHttpClient
    }

    private fun obtainGetUrl(
        url: String,
        param: KParams?
    ): String {
        if (param != null) {
            val httpUrlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: return ""
            for ((name, value) in param.headers) {
                httpUrlBuilder.addQueryParameter(name, value)
            }
            return httpUrlBuilder.build().toString()
        }
        return ""
    }

    private fun builderParams(requestBuilder: Request.Builder, param: KParams?) {
        if (param != null) {
            requestBuilder.tag(param.tag)
            addHeaders(requestBuilder, param.headers)
        }
    }

    private fun addHeaders(
        requestBuilder: Request.Builder,
        headers: Map<String, String>
    ) {
        if (headers.isEmpty()) return
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }
    }

    private fun handleSuccess(response: Response, callback: RequestCallback?) {
        val result = response.body?.string()
        if (result != null) {
            mHandler.post { callback?.onSuccess(result) }
        } else {
            handleError(Exception("response'body is null"), callback)
        }
    }

    private fun handleError(e: Exception, callback: RequestCallback?) {
        mHandler.post { callback?.onError(e) }
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

}