package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.request.base.EmptyRequest
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 17:39
 * description：
 **/
class GetRequest(
    url: String,
    params: Params
) :
    EmptyRequest<GetRequest>(Method.GET, url, params) {

    override fun obtainRequest(requestBody: RequestBody): Request {
        return obtainRequestBuilder().url(mUrl).get().tag(params.tag).build()
    }


}