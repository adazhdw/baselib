package com.adazhdw.ktlib.hihttp.callback

import org.json.JSONObject

/**
 * Created by adazhdw on 2019/12/31.
 */
abstract class JsonHttpCallback : OkHttpCallback {

    abstract fun onSuccess(data: JSONObject)

    override fun onError(e: Exception) {

    }

}