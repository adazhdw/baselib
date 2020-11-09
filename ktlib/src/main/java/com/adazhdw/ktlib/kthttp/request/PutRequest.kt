package com.adazhdw.ktlib.kthttp.request

import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.request.base.BodyRequest

/**
 * author：daguozhu
 * date-time：2020/9/3 17:21
 * description：
 **/
class PutRequest(
    url: String,
    params: Params
) : BodyRequest<PutRequest>(Method.PUT, url, params)