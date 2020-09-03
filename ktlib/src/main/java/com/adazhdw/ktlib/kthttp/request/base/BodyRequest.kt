package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.param.Param
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
    param: Param,
    callback: RequestCallback?
) : BaseRequest<R>(method, url, param, callback) {

    override fun getRequestBody(): RequestBody = param.getRequestBody()

    protected fun obtainRequestBuilder(): Request.Builder {
        return addHeaders(Request.Builder(), param.headers)
    }

}