package com.adazhdw.net

import android.os.Build
import android.text.TextUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import java.util.*

/**
 * author：daguozhu
 * date-time：2020/11/17 16:40
 * description：请求头封装
 **/
class HttpHeaders {

    companion object {

        const val TIME_FORMAT_HTTP = "EEE, dd MMM y HH:mm:ss 'GMT'"
        val TIME_ZONE_GMT: TimeZone = TimeZone.getTimeZone("GMT")

        const val KEY_ACCEPT = "Accept"
        const val VALUE_ACCEPT_ALL = "*/*"
        const val KEY_ACCEPT_ENCODING = "Accept-Encoding"
        const val VALUE_ACCEPT_ENCODING = "gzip, deflate"
        const val KEY_ACCEPT_LANGUAGE = "Accept-Language"
        val VALUE_ACCEPT_LANGUAGE: String = getLanguage()
        const val KEY_ACCEPT_RANGE = "Accept-Range"
        const val KEY_COOKIE = "Cookie"
        const val KEY_CONTENT_DISPOSITION = "Content-Disposition"
        const val KEY_CONTENT_ENCODING = "Content-Encoding"
        const val KEY_CONTENT_LENGTH = "Content-Length"
        const val KEY_CONTENT_RANGE = "Content-Range"
        const val KEY_CONTENT_TYPE = "Content-Type"
        const val VALUE_APPLICATION_URLENCODED = "application/x-www-form-urlencoded"
        const val VALUE_APPLICATION_FORM = "multipart/form-data"
        const val VALUE_APPLICATION_STREAM = "application/octet-stream"
        const val VALUE_APPLICATION_JSON = "application/json"
        const val VALUE_APPLICATION_XML = "application/xml"
        const val KEY_CACHE_CONTROL = "Cache-Control"
        const val KEY_CONNECTION = "Connection"
        const val VALUE_KEEP_ALIVE = "keep-alive"
        const val VALUE_CLOSE = "close"
        const val KEY_DATE = "Date"
        const val KEY_EXPIRES = "Expires"
        const val KEY_E_TAG = "ETag"
        const val KEY_HOST = "Host"
        const val KEY_IF_MODIFIED_SINCE = "If-Modified-Since"
        const val KEY_IF_NONE_MATCH = "If-None-Match"
        const val KEY_LAST_MODIFIED = "Last-Modified"
        const val KEY_LOCATION = "Location"
        const val KEY_RANGE = "Range"
        const val KEY_SET_COOKIE = "Set-Cookie"
        const val KEY_USER_AGENT = "User-Agent"
        val VALUE_USER_AGENT: String = getUserAgent()

        val MEDIA_TYPE_PLAIN: MediaType = "text/plain;charset=utf-8".toMediaType()
        val MEDIA_TYPE_STREAM: MediaType = "application/octet-stream".toMediaType()
        val MEDIA_TYPE_JSON: MediaType = "application/json; charset=utf-8".toMediaType()
        val PNG = "image/png; charset=UTF-8".toMediaType()
        val JPG = "image/jpeg; charset=UTF-8".toMediaType()

        private var acceptLanguage: String? = ""
        private var userAgent: String? = ""

        /**
         * Accept-Language: zh-CN,zh;q=0.8
         */
        @JvmStatic
        fun getLanguage(): String {
            if (acceptLanguage.isNullOrBlank()) {
                val locale = Locale.getDefault()
                val language = locale.language
                val country = locale.country
                val builder = StringBuilder(language)
                if (!country.isNullOrBlank()) builder.append('-').append(country).append(',').append(";q=0.8")
                acceptLanguage = builder.toString()
                return acceptLanguage!!
            }
            return acceptLanguage!!
        }

        /**
         * Get User-Agent.
         */
        @JvmStatic
        fun getUserAgent(): String {
            val webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/534.30 (KHTML, like Gecko) Version/5.0 %sSafari/534.30"
            if (userAgent.isNullOrBlank()) {
                val buffer = StringBuffer()
                buffer.append(Build.VERSION.RELEASE).append("; ")
                val locale = Locale.getDefault()
                val language = locale.language
                if (TextUtils.isEmpty(language)) {
                    buffer.append(language.toLowerCase(locale))
                    val country = locale.country
                    if (!TextUtils.isEmpty(country)) {
                        buffer.append("-")
                        buffer.append(country.toLowerCase(locale))
                    }
                } else {
                    buffer.append("en")
                }
                if ("REL" == Build.VERSION.CODENAME) {
                    if (Build.MODEL.isNotEmpty()) {
                        buffer.append("; ")
                        buffer.append(Build.MODEL)
                    }
                }
                if (Build.ID.isNotEmpty()) {
                    buffer.append(" Api/")
                    buffer.append(Build.ID)
                }
                userAgent = String.format(webUserAgent, buffer, "Mobile ")
                return userAgent!!
            }
            return userAgent!!
        }
    }
}