package com.adazhdw.ktlib.kthttp.exception

/**
 * author：daguozhu
 * date-time：2020/9/2 11:00
 * description：
 **/

open class NetException(val code: Int, val msg: String) : Exception(msg)

class NetWorkUnAvailableException(code: Int) : NetException(code, "network unavailable")

class RequestTimeoutException(code: Int) : NetException(code, "request timeout")

class RequestCanceledException(code: Int) : NetException(code, "request canceled")
