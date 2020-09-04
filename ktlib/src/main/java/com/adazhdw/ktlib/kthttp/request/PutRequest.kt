package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.kthttp.callback.RequestCallback
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.request.base.BodyRequest
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 17:21
 * description：
 **/
class PutRequest(
    url: String,
    params: Params,
    callback: RequestCallback?
) :
    BodyRequest<PutRequest>(Method.PUT, url, params, callback) {

    override fun obtainRequest(requestBody: RequestBody): Request {
        return obtainRequestBuilder().put(requestBody).url(url).tag(params.tag).build()
    }

}