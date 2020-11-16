package com.adazhdw.ktlib.kthttp.ext

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.core.lifecycle.addOnDestroy
import com.adazhdw.ktlib.kthttp.KtHttp.Companion.ktHttp
import com.adazhdw.ktlib.kthttp.callback.RequestJsonCallback
import com.adazhdw.ktlib.kthttp.model.Method
import com.adazhdw.ktlib.kthttp.model.Params
import com.adazhdw.ktlib.kthttp.request.RequestCall

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: 网络请求扩展类
 */

inline fun <reified T : Any> LifecycleOwner.getRequest(
    url: String,
    params: Params = Params.Builder().setTag(url).build(),
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    val request = netRequest(url, params, Method.GET, success, error)
    addOnDestroy { request.cancel() }
}

inline fun <reified T : Any> LifecycleOwner.postRequest(
    url: String,
    params: Params = Params.Builder().setTag(url).build(),
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    val request = netRequest(url, params, Method.POST, success, error)
    addOnDestroy { request.cancel() }
}

inline fun <reified T : Any> netRequest(
    url: String,
    params: Params = Params.Builder().setTag(url).build(),
    method: Method = Method.GET,
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
): RequestCall {
    return ktHttp.request(method, url, params, object : RequestJsonCallback<T>() {

        override fun onError(code: Int, msg: String?) {
            error.invoke(code, msg)
        }

        override fun onSuccess(data: T) {
            success.invoke(data)
        }
    })
}
