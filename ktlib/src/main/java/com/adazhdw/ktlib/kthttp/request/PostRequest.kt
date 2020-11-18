package com.adazhdw.ktlib.kthttp.request

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.ktlib.kthttp.request.base.BodyRequest
import okhttp3.Request
import okhttp3.RequestBody

/**
 * author：daguozhu
 * date-time：2020/9/3 17:21
 * description：
 **/
class PostRequest(url: String, param: Param, lifecycleOwner: LifecycleOwner) :
    BodyRequest(url, param, lifecycleOwner) {
    override fun getRequest(requestBody: RequestBody): Request {
        return requestBuilder().post(requestBody).url(url).tag(tag).build()
    }

}