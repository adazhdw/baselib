package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.RequestGsonCallback
import com.adazhdw.ktlib.kthttp.param.Param

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description: 网络请求扩展类
 */

inline fun <reified T : Any> getRequest(
    url: String,
    param: Param? = null,
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    KtHttp.get(url, param, object : RequestGsonCallback<T>() {

        override fun onError(code: Int, msg: String?) {
            error.invoke(code, msg)
        }

        override fun onSuccess(data: T) {
            success.invoke(data)
        }
    })
}

inline fun <reified T : Any> postRequest(
    url: String,
    param: Param? = null,
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    KtHttp.post(url, param, object : RequestGsonCallback<T>() {

        override fun onError(code: Int, msg: String?) {
            error.invoke(code, msg)
        }

        override fun onSuccess(data: T) {
            success.invoke(data)
        }
    })
}
