package com.adazhdw.ktlib.hihttp.callback

/**
 * Created by adazhdw on 2019/12/31.
 */
abstract class RawHttpCallback : OkHttpCallback {

    abstract fun onSuccess(data: String)

    override fun onError(e: Exception) {

    }
}