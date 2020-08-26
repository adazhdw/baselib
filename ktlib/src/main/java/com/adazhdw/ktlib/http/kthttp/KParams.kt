package com.adazhdw.ktlib.http.kthttp

import com.adazhdw.ktlib.http.kthttp.KHttp.Companion.JSON
import okhttp3.FormBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:
 */
class KParams(val tag: String = "") {
    var headers: Map<String, String> = mapOf()
        private set
    var jsonBody: String = ""
        private set
    var formBody: Boolean = false
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

    class Builder constructor(private val formBody: Boolean = false) {
        private var mTag: String = ""
        private var jsonBody: String = ""
        private var mHeaders: Map<String, String> = mapOf()
        fun setTag(mTag: String): Builder {
            this.mTag = mTag
            return this
        }

        fun setJsonBody(jsonBody: String): Builder {
            this.jsonBody = jsonBody
            return this
        }

        fun setHeaders(mHeaders: Map<String, String>): Builder {
            this.mHeaders = mHeaders
            return this
        }

        fun build(): KParams {
            val option = KParams(mTag)
            option.headers = mHeaders
            option.jsonBody = jsonBody
            option.formBody = formBody
            return option
        }
    }
}