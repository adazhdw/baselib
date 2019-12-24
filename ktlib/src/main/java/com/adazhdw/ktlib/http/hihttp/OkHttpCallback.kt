package com.adazhdw.ktlib.http.hihttp

interface OkHttpCallback {
    fun onSuccess(data:String)
    fun onError(e:Exception)
}