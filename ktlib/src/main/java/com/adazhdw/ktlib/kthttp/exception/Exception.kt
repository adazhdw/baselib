package com.adazhdw.ktlib.kthttp.exception

import java.io.IOException

/**
 * author：daguozhu
 * date-time：2020/9/2 11:00
 * description：
 **/
class NetWorkUnAvailableException : Exception("network unavailable")

class RequestCancelException : IOException("request is canceled")
