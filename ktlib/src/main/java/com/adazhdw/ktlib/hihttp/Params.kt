package com.adazhdw.ktlib.hihttp

import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody



val CONTENT_TYPE_JSON = ("application/json; charset=utf-8").toMediaTypeOrNull()
val CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=utf-8".toMediaTypeOrNull()
val CONTENT_TYPE_FILE = "application/octet-stream".toMediaTypeOrNull()//（ 二进制流，不知道下载文件类型）

class Params {

    companion object {
        fun jsonParam(url: String): Params = Params(url, CONTENT_TYPE_JSON, false)
    }

    val params: MutableMap<String, String> = mutableMapOf()
    val needHeaders: Boolean
    val url: String
    private val jsonParams: MutableMap<String, Any> = mutableMapOf()
    private val contentType: MediaType?

    constructor(
        url: String,
        contentType: MediaType? = CONTENT_TYPE_FORM,
        needHeaders: Boolean = false
    ) {
        this.url = url
        this.contentType = contentType
        this.needHeaders = needHeaders
    }

    fun put(key: String, value: Any) {
        if (isFormContent()) {
            params[key] = value.toString()
        } else {
            jsonParams[key] = value
        }
    }

    fun put(map: Map<String, Any>) {
        for ((key, value) in map) {
            if (isFormContent()) {
                params[key] = value.toString()
            } else {
                jsonParams[key] = value
            }
        }
    }

    fun postRequestBody(): RequestBody {
        return if (isFormContent()) {
            val builder = FormBody.Builder()
            for ((key, value) in params) {
                builder.add(key, value.toString())
            }
            builder.build()
        } else {
            gson.toJson(jsonParams).toRequestBody(CONTENT_TYPE_JSON)
        }
    }

    private fun isFormContent(): Boolean {
        return contentType == CONTENT_TYPE_FORM
    }
}