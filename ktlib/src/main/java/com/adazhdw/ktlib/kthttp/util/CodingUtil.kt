package com.adazhdw.ktlib.kthttp.util

import java.net.URLDecoder
import java.net.URLEncoder

/**
 * author：daguozhu
 * date-time：2020/11/3 19:30
 * description：
 **/
object CodingUtil {
    fun encode(string: String): String {
        return URLEncoder.encode(string, "utf-8")
    }

    fun decode(string: String): String {
        return URLDecoder.decode(string, "utf-8")
    }
}