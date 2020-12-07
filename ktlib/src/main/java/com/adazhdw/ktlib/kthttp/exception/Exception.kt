package com.adazhdw.ktlib.kthttp.exception

import com.adazhdw.ktlib.kthttp.constant.HttpConstant

/**
 * author：daguozhu
 * date-time：2020/9/2 11:00
 * description：
 **/

open class NetException(val code: Int, val msg: String) : Exception(msg)

class NetWorkUnAvailableException :
    NetException(HttpConstant.ERROR_NETWORK_UNAVAILABLE, "network unavailable")

class RequestTimeoutException :
    NetException(HttpConstant.ERROR_REQUEST_TIMEOUT_ERROR, "request timeout")

class RequestCanceledException :
    NetException(HttpConstant.ERROR_REQUEST_CANCEL_ERROR, "request canceled")
