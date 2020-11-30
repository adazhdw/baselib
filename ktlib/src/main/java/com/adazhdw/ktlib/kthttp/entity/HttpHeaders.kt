package com.adazhdw.ktlib.kthttp.entity

import com.adazhdw.ktlib.kthttp.KtHttp

/**
 * author：daguozhu
 * date-time：2020/11/17 16:40
 * description：请求头封装
 **/
class HttpHeaders {
    /** 请求头存放集合 */
    internal val mHeaders: HashMap<String, String> = KtHttp.ktHttp.getCommonHeaders()

    fun put(key: String, value: String) {
        mHeaders[key] = value
    }

    fun putAll(headers: Map<String, String>) {
        mHeaders.putAll(headers)
    }

    fun remove(key: String) {
        mHeaders.remove(key)
    }

    fun get(key: String): String? = mHeaders[key]

    fun isEmpty(): Boolean = mHeaders.isEmpty()

}