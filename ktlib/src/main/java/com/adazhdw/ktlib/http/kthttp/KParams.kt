package com.adazhdw.ktlib.http.kthttp

import com.adazhdw.ktlib.http.kthttp.KHttp.Companion.JSON
import com.adazhdw.ktlib.utils.MimeUtil
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:
 */
class KParams(val tag: String = "") {
    var headers: Map<String, String> = mapOf()
        private set
    var files: Map<String, File> = mapOf()
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

    fun getFileRequestBody(): MutableMap<String, RequestBody> {
        val map = mutableMapOf<String, RequestBody>()
        for ((key, file) in files) {
            val path = file.path
            map[key] = file.asRequestBody(MimeUtil.getMimeType(path).toMediaTypeOrNull())
        }
        return map
    }

    class Builder {
        private var mTag: String = ""
        private var jsonBody: String = ""
        private var mHeaders: Map<String, String> = mapOf()
        private var files: Map<String, File> = mapOf()
        private var formBody: Boolean = false
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
            option.files = files
            return option
        }
    }
}