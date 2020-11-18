package com.adazhdw.ktlib.kthttp.ext

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.core.lifecycle.addOnDestroy
import com.adazhdw.ktlib.kthttp.KtHttp.Companion.ktHttp
import com.adazhdw.ktlib.kthttp.callback.RequestJsonCallback
import com.adazhdw.ktlib.kthttp.constant.Method
import com.adazhdw.ktlib.kthttp.model.Param
import com.adazhdw.ktlib.kthttp.request.base.BaseRequest

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: 网络请求扩展类
 */

inline fun <reified T : Any> LifecycleOwner.getRequest(
    url: String,
    param: Param = Param.build(),
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    val request = netRequest(this, url, param, Method.GET, success, error)
    addOnDestroy { request.cancel() }
}

inline fun <reified T : Any> LifecycleOwner.postRequest(
    url: String,
    param: Param = Param.build(),
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    val request = netRequest(this, url, param, Method.POST, success, error)
    addOnDestroy { request.cancel() }
}

inline fun <reified T : Any> netRequest(
    lifecycleOwner: LifecycleOwner,
    url: String,
    param: Param = Param.build(),
    method: Method = Method.GET,
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
): BaseRequest {
    return ktHttp.request(method, url, param, object : RequestJsonCallback<T>(lifecycleOwner) {

        override fun onError(code: Int, msg: String?) {
            error.invoke(code, msg)
        }

        override fun onSuccess(data: T) {
            success.invoke(data)
        }
    })
}
