package com.adazhdw.ktlib.hihttp

interface OkHttpCallback {
    fun onSuccess(data:String)
    fun onError(e:Exception)
}