package com.adazhdw.ktlib.kthttp.model

import okhttp3.MediaType.Companion.toMediaType


/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:
 */
object HttpConstant {
    const val DEFAULT_TIMEOUT = 15L
    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50 // 50M 的缓存大小

    const val ERROR_RESPONSE_ON_FAILURE = 1001
    const val ERROR_RESPONSE_BODY_ISNULL = 1002
    const val ERROR_JSON_PARSE_EXCEPTION = 1003
    const val ERROR_NETWORK_UNAVAILABLE = 1004


    val JSON = "application/json; charset=utf-8".toMediaType()
    val PNG = "image/png; charset=UTF-8".toMediaType()
    val JPG = "image/jpeg; charset=UTF-8".toMediaType()

}