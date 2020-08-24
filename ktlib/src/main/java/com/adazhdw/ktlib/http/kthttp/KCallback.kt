package com.adazhdw.ktlib.http.kthttp

/**
 * Author: dgz
 * Date: 2020/8/21 15:28
 * Description: 网络请求自定义回调
 */
interface KCallback {
    fun onSuccess(result: String)
    fun onError(e: Exception)
}