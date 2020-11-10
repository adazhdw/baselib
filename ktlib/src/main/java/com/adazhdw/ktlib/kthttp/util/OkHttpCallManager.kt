package com.adazhdw.ktlib.kthttp.util

import android.text.TextUtils.isEmpty
import okhttp3.Call
import java.util.concurrent.ConcurrentHashMap

/**
 * author：daguozhu
 * date-time：2020/9/1 17:02
 * description：
 **/
internal class OkHttpCallManager private constructor() {
    private val callMap: ConcurrentHashMap<String, Call> = ConcurrentHashMap()
    fun addCall(url: String, call: Call?) {
        if (call != null && !isEmpty(url)) {
            callMap[url] = call
        }
    }

    fun getCall(url: String): Call? {
        return if (!isEmpty(url)) {
            callMap[url]
        } else null
    }

    fun removeCall(url: String) {
        if (!isEmpty(url)) {
            callMap.remove(url)
        }
    }

    companion object {
        val callManager: OkHttpCallManager by lazy { OkHttpCallManager() }
    }

}