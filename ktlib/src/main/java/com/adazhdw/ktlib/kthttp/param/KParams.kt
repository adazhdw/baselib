package com.adazhdw.ktlib.kthttp.param

import com.adazhdw.ktlib.http.KHttp.Companion.JSON
import okhttp3.FormBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:
 */
class KParams(var tag: String = "") {
    var headers: Map<String, String> = mapOf()
        private set
    var params: Map<String, String> = mapOf()
        private set
    var jsonBody: String = ""
        private set
    var formBody: Boolean = false
        private set

    /**
     * URL编码，只对GET,DELETE,HEAD有效
     */
    var urlEncoder: Boolean = false
        private set

    fun getRequestBody(): RequestBody {
        return if (formBody) getParamFormBody()
        else getJsonRequestBody()
    }

    private fun getJsonRequestBody(): RequestBody {
        return jsonBody.toRequestBody(JSON)
    }

    private fun getParamFormBody(): FormBody {
        return FormBody.Builder().apply {
            for ((name, value) in headers) {
                add(name, value)
            }
        }.build()
    }

    class Builder constructor(private val formBody: Boolean = true) {
        private var mTag: String = ""
        private var urlEncoder: Boolean = false
        private var jsonBody: String = ""
        private var mHeaders: MutableMap<String, String> = mutableMapOf()
        private val params: MutableMap<String, String> = mutableMapOf()
        fun setTag(mTag: String): Builder {
            this.mTag = mTag
            return this
        }

        fun setIsUrlEncoder(urlEncoder: Boolean): Builder {
            this.urlEncoder = urlEncoder
            return this
        }

        fun setJsonBody(jsonBody: String): Builder {
            this.jsonBody = jsonBody
            return this
        }

        fun addHeaders(mHeaders: Map<String, String>): Builder {
            this.mHeaders.putAll(mHeaders)
            return this
        }

        fun addHeader(key: String, value: String): Builder {
            this.mHeaders[key] = value
            return this
        }

        fun addParam(key: String, value: String): Builder {
            this.params[key] = value
            return this
        }

        fun addParams(paramMap: Map<String, String>): Builder {
            this.params.putAll(paramMap)
            return this
        }

        fun build(): KParams {
            val option = KParams(mTag)
            option.headers = mHeaders
            option.params = params
            option.urlEncoder = urlEncoder
            option.jsonBody = jsonBody
            option.formBody = formBody
            return option
        }
    }
}