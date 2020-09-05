package com.adazhdw.ktlib.kthttp.util

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * author：daguozhu
 * date-time：2020/9/1 17:02
 * description：
 **/

object RequestUrlUtil {
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

    fun getFullUrl2(url: String, params: Map<String, String>, urlEncoder: Boolean): String {
        val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: return ""
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
            if (urlEncoder) {
                urlBuilder.addEncodedQueryParameter(name, value)
            } else {
                urlBuilder.addQueryParameter(name, value)
            }
        }
        return urlBuilder.build().toString()
    }

}
