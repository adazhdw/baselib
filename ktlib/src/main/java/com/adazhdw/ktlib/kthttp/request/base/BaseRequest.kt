package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.kthttp.KtHttp.Companion.ktHttp
import com.adazhdw.ktlib.kthttp.model.Params
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 10:11
 * description：
 **/
abstract class BaseRequest(
    val url: String,
    val params: Params
) {
    private val commonHeaders = ktHttp.getCommonHeaders()
    private val builder = Request.Builder()

    abstract fun getRequestBody(): RequestBody

    abstract fun getRequest(requestBody: RequestBody): Request

    /**
     * 生成一个 Request.Builder，并且给当前请求 Request 添加 headers
     */
    protected fun requestBuilder(): Request.Builder {
        if (params.needHeaders) {
            if (commonHeaders.isNotEmpty()) builder.headers(ktHttp.getCommonHttpHeaders())
            for ((key, value) in params.headers.mHeaders) builder.addHeader(key, value)
        }
        return builder
    }

    open fun getRealUrl() = url
}