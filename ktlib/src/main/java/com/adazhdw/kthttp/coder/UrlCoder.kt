package com.adazhdw.kthttp.coder

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * author：daguozhu
 * date-time：2020/11/6 15:46
 * description：
 */
class UrlCoder private constructor() : ICoder {


    companion object {
        fun create(): UrlCoder {
            return UrlCoder()
        }
    }


    override fun encode(string: String): String {
        try {
            return URLEncoder.encode(string, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ""
    }

    override fun decode(string: String): String {
        try {
            return URLDecoder.decode(string, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ""
    }
}