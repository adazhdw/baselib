package com.adazhdw.ktlib.kthttp.model

import com.adazhdw.ktlib.kthttp.coder.UrlCoder
import com.adazhdw.ktlib.kthttp.constant.HttpConstant
import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:请求参数工具类
 */
/** @param isMultipart 是否有流参数 */
class Param private constructor(isMultipart: Boolean) {

    internal val headers: HttpHeaders = HttpHeaders()
    internal val params: HttpParams = HttpParams(isMultipart)
    private var jsonBody: String = ""
    private var isJsonRequest: Boolean = false
    private val urlCoder = UrlCoder.create()

    /**
     * URL编码，只对GET,DELETE,HEAD有效
     */
    internal var urlEncoder: Boolean = false
    internal var needHeaders: Boolean = false

    internal fun getRequestBody(): RequestBody {
        return if (isJsonRequest) getJsonRequestBody()
        else getParamFormBody()
    }

    private fun getJsonRequestBody(): RequestBody {
        return if (jsonBody.isNotBlank()) {
            jsonBody.toRequestBody(HttpConstant.JSON)
        } else {
            val jsonObject = JSONObject()
            for ((key, value) in params.mParams) jsonObject.put(key, value)
            jsonObject.toString().toRequestBody(HttpConstant.JSON)
        }
    }

    private fun getParamFormBody(): RequestBody {
        if (params.isMultipart && params.files.isNotEmpty()) {
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            for (part in params.files) {
                builder.addFormDataPart(
                    part.key,
                    urlCoder.encode(part.wrapper.fileName),
                    part.wrapper.file.asRequestBody(part.wrapper.mediaType)
                )
            }
            for ((key, value) in params.mParams) {
                builder.addFormDataPart(key, value.toString())
            }
            return builder.build()
        } else {
            return FormBody.Builder().apply {
                for ((name, value) in params.mParams) {
                    add(name, value.toString())
                }
            }.build()
        }
    }


    fun setUrlEncoder(urlEncoder: Boolean): Param {
        this.urlEncoder = urlEncoder
        return this
    }

    fun setNeedHeaders(needHeaders: Boolean): Param {
        this.needHeaders = needHeaders
        return this
    }

    fun setJsonBody(jsonBody: String): Param {
        this.jsonBody = jsonBody
        return this
    }

    fun addHeaders(headers: Map<String, String>): Param {
        this.headers.putAll(headers)
        return this
    }

    fun addHeader(key: String, value: String): Param {
        this.headers.put(key, value)
        return this
    }

    fun addParam(key: String, value: String): Param {
        this.params.put(key, value)
        return this
    }

    fun addParams(paramMap: Map<String, String>): Param {
        this.params.putAll(paramMap)
        return this
    }

    companion object {
        fun build(isMultipart: Boolean = false) = Param(isMultipart)
    }

}