package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.kthttp.entity.Param
import com.adazhdw.ktlib.kthttp.request.base.BodyRequest
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 17:21
 * description：
 **/
class PatchRequest(url: String, param: Param) : BodyRequest(url, param) {

    override fun getRequest(requestBody: RequestBody): Request {
        return requestBuilder().patch(requestBody).url(url).tag(tag).build()
    }

}