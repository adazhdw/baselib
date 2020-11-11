package com.adazhdw.ktlib.kthttp.util

import android.text.TextUtils.isEmpty
import com.adazhdw.ktlib.kthttp.model.Params
import okhttp3.Call
import java.util.concurrent.ConcurrentHashMap

/**
 * author：daguozhu
 * date-time：2020/9/1 17:02
 * description：
 **/
internal class OkHttpCallManager private constructor() {
    private val callMap: ConcurrentHashMap<String, Call> = ConcurrentHashMap()
    fun addCall(url: String, params: Params, call: Call?): Boolean {//如果isCalling,返回 false 取消重复请求
        val key = url + listOf(params.headers, params.params).toString()
        if (call != null && !isEmpty(key) && !callMap.containsKey(key)) {
            callMap[key] = call
            return true//添加成功说明没有重复请求
        }
        return false
    }

    fun getCall(url: String, params: Params): Call? {
        val key = url + listOf(params.headers, params.params).toString()
        return if (!isEmpty(key)) {
            callMap[key]
        } else null
    }

    fun removeCall(url: String, params: Params) {
        val key = url + listOf(params.headers, params.params).toString()
        if (!isEmpty(key)) {
            callMap.remove(key)
        }
    }

    companion object {
        val callManager: OkHttpCallManager by lazy { OkHttpCallManager() }
    }

}