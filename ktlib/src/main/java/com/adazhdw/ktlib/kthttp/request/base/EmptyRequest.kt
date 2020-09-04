package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.kthttp.KtHttp.Companion.ktHttp
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.util.RequestUrlUtil
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 16:25
 * description：
 **/
abstract class EmptyRequest<R : EmptyRequest<R>>(
    method: Method,
    url: String,
    params: Params
) :
    BaseRequest<R>(method, url, params) {
    protected val mUrl: String

    init {
        val commonParams = mutableMapOf<String, String>().apply {
            putAll(ktHttp.getCommonParams())
            putAll(params.params)
        }
        mUrl = RequestUrlUtil.getFullUrl(url, commonParams, params.urlEncoder)
    }

    final override fun getRequestBody(): RequestBody = FormBody.Builder().build()

    protected fun obtainRequestBuilder(): Request.Builder {
        return addHeaders(Request.Builder(), params.headers)
    }

}