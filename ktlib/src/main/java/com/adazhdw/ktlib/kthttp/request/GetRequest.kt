package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.kthttp.model.Param
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
    param: Param
) : EmptyRequest(url, param) {

    override fun getRequest(requestBody: RequestBody): Request {
        return requestBuilder().url(getRealUrl()).get().tag(tag).build()
    }


}