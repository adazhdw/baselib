package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.kthttp.model.Param
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 10:11
 * description：
 **/
abstract class BaseRequest(
    val url: String,
    val param: Param
) {
    protected var tag = ""

    abstract fun getRequestBody(): RequestBody

    abstract fun getRequest(requestBody: RequestBody): Request

    /**
     * 生成一个 Request.Builder，并且给当前请求 Request 添加 headers
     */
    protected fun requestBuilder(): Request.Builder {
        val builder = Request.Builder()
        if (param.needHeaders) {
            for ((key, value) in param.headers.mHeaders) {
                builder.addHeader(key, value)
            }
        }
        return builder
    }

    open fun getRealUrl() = url

    open fun setTag(tag: Any): BaseRequest {
        setTag(tag.toString())
        return this
    }

    open fun setTag(tag: String): BaseRequest {
        this.tag = tag
        return this
    }
}