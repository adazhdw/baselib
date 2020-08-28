package com.adazhdw.ktlib.kthttp.bean

import okhttp3.Headers
import okhttp3.Response


data class ResponseData(
    var successful: Boolean = false,
    var code: Int = 0,
    var msg: String = "",
    var result: String = "",
    var headers: Headers = Headers.headersOf(),
    var responseNull: Boolean = false,
    var timeout: Boolean = false,
    var httpResponse: Response? = null
)