package com.adazhdw.ktlib.kthttp

import com.adazhdw.ktlib.kthttp.callback.RequestGsonCallback
import com.adazhdw.ktlib.kthttp.param.KParams

/**
 * 扩展方法
 */

inline fun <reified T : Any> getRequest(
    url: String,
    params: KParams? = null,
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    KtHttp.get(url, params, object : RequestGsonCallback<T>() {

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
    params: KParams? = null,
    crossinline success: ((data: T) -> Unit),
    crossinline error: ((code: Int, msg: String?) -> Unit)
) {
    KtHttp.post(url, params, object : RequestGsonCallback<T>() {

        override fun onError(code: Int, msg: String?) {
            error.invoke(code, msg)
        }

        override fun onSuccess(data: T) {
            success.invoke(data)
        }
    })
}
