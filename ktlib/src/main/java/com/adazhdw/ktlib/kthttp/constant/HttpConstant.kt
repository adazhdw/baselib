package com.adazhdw.ktlib.kthttp.constant


/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:
 */
object HttpConstant {
    const val DEFAULT_TIMEOUT = 15L
    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50 // 50M 的缓存大小
    var debug = true

    const val ERROR_RESPONSE_ON_FAILURE = 1001
    const val ERROR_RESPONSE_BODY_ISNULL = 1002
    const val ERROR_JSON_PARSE_EXCEPTION = 1003
    const val ERROR_NETWORK_UNAVAILABLE = 1004
}