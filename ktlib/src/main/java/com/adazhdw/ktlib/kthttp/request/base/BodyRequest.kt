package com.adazhdw.ktlib.kthttp.request.base

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.kthttp.model.Param
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/11/17 20:20
 * description：
 **/
abstract class BodyRequest(url: String, param: Param, lifecycleOwner: LifecycleOwner) :
    BaseRequest(url, param, lifecycleOwner) {
    override fun getRequestBody(): RequestBody = param.getRequestBody()
}
