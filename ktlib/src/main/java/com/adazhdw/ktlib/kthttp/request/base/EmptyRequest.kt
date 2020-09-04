package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.kthttp.callback.RequestCallback
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
    params: Params,
    callback: RequestCallback?
) :
    BaseRequest<R>(method, url, params, callback) {
    protected val mUrl: String
        get() = RequestUrlUtil.getFullUrl(url, params.params, params.urlEncoder)

    final override fun getRequestBody(): RequestBody = FormBody.Builder().build()

    protected fun obtainRequestBuilder(): Request.Builder {
        return addHeaders(Request.Builder(), params.headers)
    }

}