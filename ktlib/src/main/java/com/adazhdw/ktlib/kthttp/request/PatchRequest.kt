package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 17:21
 * description：
 **/
class PatchRequest(
    url: String,
    param: Param
) : BaseRequest(url, param) {

    override fun getRequestBody(): RequestBody = param.getRequestBody()

    override fun getRequest(requestBody: RequestBody): Request {
        return requestBuilder().patch(requestBody).url(url).tag(tag).build()
    }

}