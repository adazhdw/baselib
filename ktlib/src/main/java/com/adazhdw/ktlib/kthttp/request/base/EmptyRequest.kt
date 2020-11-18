package com.adazhdw.ktlib.kthttp.request.base

import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.ktlib.kthttp.util.RequestUrlUtil
import okhttp3.RequestBody
import okhttp3.internal.EMPTY_REQUEST

/**
 * author：daguozhu
 * date-time：2020/9/3 16:25
 * description：
 **/
abstract class EmptyRequest(url: String, param: Param) : BaseRequest(url, param) {

    final override fun getRequestBody(): RequestBody = EMPTY_REQUEST

    override fun getRealUrl(): String {
        val commonParams = param.params.mParams
        return RequestUrlUtil.getFullUrl2(url, commonParams, param.urlEncoder)
    }
}