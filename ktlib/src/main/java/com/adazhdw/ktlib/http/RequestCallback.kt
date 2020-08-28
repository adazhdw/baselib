package com.adazhdw.ktlib.http

/**
 * Author: dgz
 * Date: 2020/8/21 15:28
 * Description: 网络请求自定义回调
 */
interface RequestCallback {
    fun onSuccess(result: String)
    fun onError(e: Exception)
}