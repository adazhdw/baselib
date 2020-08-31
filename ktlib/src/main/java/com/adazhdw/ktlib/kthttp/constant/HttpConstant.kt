package com.adazhdw.ktlib.kthttp.constant


object HttpConstant {
    const val DEFAULT_TIMEOUT = 15L
    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50 // 50M 的缓存大小
    var debug = true

    const val ERROR_RESPONSE_ON_FAILURE = 1001
    const val ERROR_RESPONSE_BODY_ISNULL = 1002
    const val ERROR_JSON_PARSE_EXCEPTION = 1003
}