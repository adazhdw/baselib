package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 17:08
 * description：
 **/
abstract class BodyRequest<R : BodyRequest<R>>(
    method: Method,
    url: String,
    params: Params
) : BaseRequest<R>(method, url, params) {

    override fun getRequestBody(): RequestBody = params.getRequestBody()

    protected fun obtainRequestBuilder(): Request.Builder {
        return addHeaders(Request.Builder(), params.headers)
    }

}