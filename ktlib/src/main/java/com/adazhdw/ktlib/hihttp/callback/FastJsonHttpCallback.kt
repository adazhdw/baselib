package com.adazhdw.ktlib.hihttp.callback

import com.alibaba.fastjson.TypeReference

/**
 * Created by adazhdw on 2019/12/31.
 */
abstract class FastJsonHttpCallback<T : Any> : OkHttpCallback {

    val typeReference: TypeReference<T>

    init {
        typeReference = object : TypeReference<T>() {}
    }

    abstract fun onSuccess(data: T)

    override fun onException(e: Exception) {

    }
}
