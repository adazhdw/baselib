package com.adazhdw.ktlib.kthttp.model

import com.adazhdw.ktlib.utils.MimeUtil
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:
 */
class Params internal constructor(var tag: String = "") {
    internal var headers = HttpHeaders()
        private set
    internal var params: HashMap<String, String> = hashMapOf()
        private set
    internal var files: List<Part> = listOf()
        private set
    internal var jsonBody: String = ""
        private set
    internal var isJsonType: Boolean = true
        private set

    /**
     * URL编码，只对GET,DELETE,HEAD有效
     */
    internal var urlEncoder: Boolean = false
        private set
    internal var needHeaders: Boolean = false
        private set

    internal fun getRequestBody(): RequestBody {
        return if (isJsonType) getJsonRequestBody()
        else getParamFormBody()
    }

    private fun getJsonRequestBody(): RequestBody {
        return if (jsonBody.isNotBlank()) jsonBody.toRequestBody(HttpConstant.JSON)
        else JSONObject().apply { for ((key, value) in params) put(key, value) }.toString()
            .toRequestBody(HttpConstant.JSON)
    }

    private fun getParamFormBody(): RequestBody {
        if (files.isNotEmpty()) {
            val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            for (part in files) {
                multipartBody.addFormDataPart(
                    part.key,
                    part.wrapper.fileName,
                    part.wrapper.file.asRequestBody(part.wrapper.mediaType)
                )
            }
            for ((key, value) in params) {
                multipartBody.addFormDataPart(key, value)
            }
            return multipartBody.build()
        } else {
            return FormBody.Builder().apply {
                for ((name, value) in params) {
                    add(name, value)
                }
            }.build()
        }
    }

    class Builder constructor(private val isJsonType: Boolean = false) {
        private var mTag: String = ""
        private var urlEncoder: Boolean = false
        private var needHeaders: Boolean = false
        private var jsonBody: String = ""
        private val mHeaders: HashMap<String, String> = hashMapOf()
        private val params: HashMap<String, String> = hashMapOf()
        private val files: MutableList<Part> = mutableListOf()

        fun setTag(mTag: String): Builder {
            this.mTag = mTag
            return this
        }

        fun setIsUrlEncoder(urlEncoder: Boolean): Builder {
            this.urlEncoder = urlEncoder
            return this
        }

        fun setIsNeedHeaders(needHeaders: Boolean): Builder {
            this.needHeaders = needHeaders
            return this
        }

        fun setJsonBody(jsonBody: String): Builder {
            if (jsonBody.isNotBlank()) this.jsonBody = jsonBody
            return this
        }

        fun addHeaders(mHeaders: Map<String, String>): Builder {
            if (mHeaders.isNotEmpty()) this.mHeaders.putAll(mHeaders)
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
            if (paramMap.isNotEmpty()) this.params.putAll(paramMap)
            return this
        }

        fun addFormDataPart(key: String, file: File): Builder {
            if (!file.exists() || file.length() == 0L) return this
            val isPng = file.name.indexOf("png") > 0 || file.name.indexOf("PNG") > 0
            if (isPng) {
                this.files.add(Part(key, Part.FileWrapper(file, HttpConstant.PNG)))
                return this
            }
            val isJpg = file.name.indexOf("jpg") > 0 || file.name.indexOf("JPG") > 0
                    || file.name.indexOf("jpeg") > 0 || file.name.indexOf("JPEG") > 0
            if (isJpg) {
                this.files.add(Part(key, Part.FileWrapper(file, HttpConstant.JPG)))
                return this
            }
            if (!isPng && !isJpg) {
                this.files.add(Part(key, Part.FileWrapper(file, mediaType(file))))
            }
            return this
        }

        fun addFormDataPart(map: Map<String, File>): Builder {
            for ((key, file) in map) addFormDataPart(key, file)
            return this
        }

        private fun mediaType(file: File): MediaType? = MimeUtil.getMediaType(file.path)

        fun build(): Params {
            val option = Params(mTag)
            option.headers.putAll(this.mHeaders)
            option.params = this.params
            option.files = this.files
            option.urlEncoder = this.urlEncoder
            option.needHeaders = this.needHeaders
            option.jsonBody = this.jsonBody
            option.isJsonType = this.isJsonType
            return option
        }
    }
}