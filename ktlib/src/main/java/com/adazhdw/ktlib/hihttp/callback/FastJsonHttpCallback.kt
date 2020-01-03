package com.adazhdw.ktlib.hihttp.callback

/**
 * Created by adazhdw on 2019/12/31.
 */
abstract class FastJsonHttpCallback<T : Any> : OkHttpCallback {

    abstract fun onSuccess(data: T)

    override fun onException(e: Exception) {

    }
}