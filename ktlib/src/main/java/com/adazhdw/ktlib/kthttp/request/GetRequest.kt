package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.kthttp.KtHttp
import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.param.Param
import com.adazhdw.ktlib.kthttp.request.base.EmptyRequest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 17:39
 * description：
 **/
class GetRequest(
    url: String,
    param: Param,
    callback: RequestCallback?
) :
    EmptyRequest<GetRequest>(Method.GET, url, param, callback) {
    override val okHttpClient: OkHttpClient
        get() = KtHttp.okHttpClient

    override fun obtainRequest(requestBody: RequestBody): Request {
        return obtainRequestBuilder().url(mUrl).get().tag(param.tag).build()
    }


}