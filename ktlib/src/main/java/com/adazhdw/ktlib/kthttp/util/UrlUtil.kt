package com.adazhdw.ktlib.kthttp.util

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

object UrlUtil {
    fun getFullUrl(url: String, params: Map<String, String>, urlEncoder: Boolean): String {
        val urlBuilder = StringBuilder()
        urlBuilder.append(url)
        if (!url.contains("?") && params.isNotEmpty()) {
            urlBuilder.append("?")
        }
        var flag = 0
        for ((key, keyValue) in params) {
            var name = key
            var value = keyValue
            if (urlEncoder) {
                try {
                    name = URLEncoder.encode(key, "UTF-8")
                    value = URLEncoder.encode(keyValue, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
            urlBuilder.append(name).append("=").append(value)
            if (++flag != params.size) {
                urlBuilder.append("&")
            }
        }
        return urlBuilder.toString()
    }

}
