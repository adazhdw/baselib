package com.adazhdw.ktlib.kthttp.callback

import okhttp3.Headers
import okhttp3.Response

/**
 * Author: dgz
 * Date: 2020/8/21 15:28
 * Description: 网络请求自定义回调
 */
interface BaseRequestCallback {
    fun onStart()
    fun onResponse(httpResponse: Response?, response: String?, headers: Headers)
    fun onSuccess(result: String)
    fun onError(code: Int, msg: String?)
    fun onFinish()
}