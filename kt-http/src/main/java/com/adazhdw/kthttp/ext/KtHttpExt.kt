package com.adazhdw.kthttp.ext

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.kthttp.KtHttp.Companion.ktHttp
import com.adazhdw.kthttp.callback.RequestJsonCallback
import com.adazhdw.kthttp.constant.Method
import com.adazhdw.kthttp.entity.Param

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
    netRequest(this, url, param, Method.GET, success, error)
}

inline fun <reified T : Any> LifecycleOwner.postRequest(
    url: String,
    param: Param = Param.build(),
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    netRequest(this, url, param, Method.POST, success, error)
}

inline fun <reified T : Any> netRequest(
    lifecycleOwner: LifecycleOwner?,
    url: String,
    param: Param = Param.build(),
    method: Method = Method.GET,
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    ktHttp.request(method, url, param, object : RequestJsonCallback<T>(lifecycleOwner) {

        override fun onError(code: Int, msg: String?) {
            error.invoke(code, msg)
        }

        override fun onSuccess(data: T) {
            success.invoke(data)
        }
    })
}
