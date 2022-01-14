package com.adazhdw.net

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.regex.Pattern


class RequestFactory(builder: Builder) {

    companion object {
        fun parseBuilder(net: Net): Builder {
            return Builder(net)
        }
    }

    val net: Net = builder.net
    internal val method: HttpMethod = builder.method
    private val headers: okhttp3.Headers? = builder.headersBuilder?.build()
    private val baseUrl: okhttp3.HttpUrl = if (builder.newBaseUrl != null) builder.newBaseUrl!! else net.baseUrl
    private val urlPath: String? = builder.urlPath
    private val pathParams: Map<String, String> = builder.pathParams
    private val pathParamNames: Set<String> = builder.pathParamNames
    private val queryParams: List<ParamField> = builder.queryParams
    private val hasBody: Boolean = builder.hasBody
    private val requestBody: okhttp3.RequestBody? = builder.requestBody
    private val isFormEncoded: Boolean = builder.isFormEncoded
    private val formFields: List<ParamField> = builder.formFields
    private val isMultipart: Boolean = builder.isMultipart
    private val partParams: List<PartField> = builder.partParams
    private val mTag: Any? = builder.mTag
    private val contentType: okhttp3.MediaType? = builder.contentType
    internal val isStreaming: Boolean = builder.isStreaming
    private val needCommonHeaders: Boolean = builder.needCommonHeaders
    private val needCommonParams: Boolean = builder.needCommonParams

    inline fun <reified T : Any> parseClazz(): Call<T> {
        return InternalAdapter.parse<T, Call<T>>(object : TypeRef<Call<T>>() {}.type, net, this)
    }

    inline fun <reified T : Any, reified R : Any> parseClazz(typeRef: TypeRef<R>): R {
        return InternalAdapter.parse<T, R>(typeRef.type, net, this)
    }

    fun download(): Call<okhttp3.ResponseBody> {
        return InternalAdapter.parse<okhttp3.ResponseBody, Call<okhttp3.ResponseBody>>(object : TypeRef<Call<okhttp3.ResponseBody>>() {}.type, net, this)
    }

    @Throws(IOException::class)
    fun create(): okhttp3.Request {
        val requestBuilder = RequestBuilder(method.name, baseUrl, urlPath, headers, contentType, hasBody, isFormEncoded, isMultipart)

        if (needCommonHeaders && net.commonHeaders.isNotEmpty()) {
            for ((name, value) in net.commonHeaders) {
                requestBuilder.addHeader(name, value)
            }
        }
        if (needCommonParams && net.commonParams.isNotEmpty()) {
            for (param in net.commonParams) {
                requestBuilder.addQueryParam(param.name, param.value, param.encoded)
            }
        }
        if (pathParams.isNotEmpty()) {
            for ((name, value) in pathParams) {
                if (!pathParamNames.contains(name)) {
                    throw IllegalArgumentException("URL \"$urlPath\" does not contain pathName: \"{$name}\".")
                }
                requestBuilder.addPathParam(name, value)
            }
        }
        if (queryParams.isNotEmpty()) {
            for (param in queryParams) {
                requestBuilder.addQueryParam(param.name, param.value, param.encoded)
            }
        }
        when {
            requestBody != null -> {
                requestBuilder.setBody(requestBody)
            }
            isFormEncoded -> {
                for (param in formFields) {
                    requestBuilder.addFormField(param.name, param.value, param.encoded)
                }
            }
            isMultipart -> {
                for (param in partParams) {
                    requestBuilder.addPart(
                        okhttp3.Headers.headersOf("Content-Disposition", "form-data; name=\"" + param.name + "\"", "Content-Transfer-Encoding", param.encoding),
                        param.value
                    )
                }
            }
        }
        requestBuilder.addTag(mTag)

        return requestBuilder.get().build()
    }

    class Builder(val net: Net) {
        // Upper and lower characters, digits, underscores, and hyphens, starting with a character.
        companion object {
            private const val PARAM = "[a-zA-Z][a-zA-Z0-9_-]*"
            private val PARAM_URL_REGEX = Pattern.compile("\\{($PARAM)\\}")
            private val PARAM_NAME_REGEX = Pattern.compile(PARAM)

            private fun parsePathParam(path: String): Set<String> {
                val m = PARAM_URL_REGEX.matcher(path)
                val patterns = LinkedHashSet<String>()
                while (m.find()) {
                    m.group(1)?.let {
                        patterns.add(it)
                    }
                }
                return patterns
            }
        }

        private var gotField = false
        private var gotPart = false
        private var gotBody = false
        private var gotPath = false
        private var gotQuery = false
        private var gotUrl = false
        internal var method: HttpMethod = HttpMethod.GET
        internal var hasBody = false
        internal var newBaseUrl: okhttp3.HttpUrl? = null
        internal var urlPath: String? = null
        internal var isFormEncoded = false
        internal var isMultipart = false    //是否强制使用 multipart/form-data 表单上传
        internal var headersBuilder: okhttp3.Headers.Builder? = null
        internal val pathParams by lazy { mutableMapOf<String, String>() }
        internal val pathParamNames by lazy { mutableSetOf<String>() }
        internal val queryParams by lazy { mutableListOf<ParamField>() }
        internal val formFields by lazy { mutableListOf<ParamField>() }
        internal val partParams by lazy { mutableListOf<PartField>() }
        internal var contentType: okhttp3.MediaType? = null
        internal var requestBody: okhttp3.RequestBody? = null
        internal var mTag: Any? = null
        internal var isStreaming: Boolean = false
        internal var needCommonHeaders: Boolean = false
        internal var needCommonParams: Boolean = false

        fun method(method: HttpMethod) = apply {
            this.method = method
            this.hasBody = method.hasBody
        }

        fun baseUrl(baseUrl: okhttp3.HttpUrl) = apply {
            val pathSegments = baseUrl.pathSegments
            require("" == pathSegments[pathSegments.size - 1]) { "baseUrl must end in /: $baseUrl" }
            this.newBaseUrl = baseUrl
        }

        fun baseUrl(baseUrl: String) = apply {
            baseUrl(baseUrl.toHttpUrl())
        }

        fun baseUrl(baseUrl: URL) = apply {
            baseUrl(baseUrl.toString().toHttpUrl())
        }

        fun urlPath(urlPath: String) = apply {
            this.urlPath = urlPath
            this.gotUrl = true
            this.pathParamNames.addAll(parsePathParam(urlPath))
        }

        fun headers(name: String, value: String) = apply {
            if (this.headersBuilder == null) {
                this.headersBuilder = okhttp3.Headers.Builder()
            }
            if (HttpHeaders.KEY_CONTENT_TYPE.equals(name, true)) {
                try {
                    contentType = value.toMediaType()
                } catch (e: IllegalArgumentException) {
                    throw IllegalArgumentException("Malformed content type: $value")
                }
            } else {
                this.headersBuilder?.add(name, value)
            }
        }

        fun headers(headerMap: Map<String, String>) = apply {
            for ((name, value) in headerMap) {
                headers(name, value)
            }
        }

        fun pathParams(name: String, value: String) = apply {
            if (!PARAM_NAME_REGEX.matcher(name).matches()) {
                throw IllegalArgumentException("Path parameter name must match ${PARAM_URL_REGEX.pattern()}. Found: ${name}")
            }
            this.pathParams[name] = value
            if (!this.gotPath) {
                this.gotPath = true
            }
        }

        fun pathParams(params: Map<String, String>) = apply {
            for ((name, value) in params) {
                pathParams(name, value)
            }
        }

        fun queryParams(name: String, value: String, encoded: Boolean = false) = apply {
            queryParams(ParamField(name, value, encoded))
        }

        fun queryParams(paramField: ParamField) = apply {
            this.queryParams.add(paramField)
            if (!this.gotQuery) {
                this.gotQuery = true
            }
        }

        fun isFormEncoded() = apply {
            require(!isMultipart) { "isFormEncoded() or isMultipart() only can be called one of them" }
            isFormEncoded = true
        }

        fun formFields(name: String, value: String, encoded: Boolean) = apply {
            formFields(ParamField(name, value, encoded))
        }

        fun formFields(name: String, value: String) = apply {
            formFields(ParamField(name, value, false))
        }

        fun formFields(paramField: ParamField) = apply {
            if (!this.gotField) {
                this.gotField = true
            }
            this.formFields.add(paramField)
        }

        fun isMultipart() = apply {
            require(!isFormEncoded) { "isFormEncoded() or isMultipart() only can be called one of them" }
            isMultipart = true
        }

        fun partString(name: String, value: String, mediaType: okhttp3.MediaType = HttpHeaders.MEDIA_TYPE_PLAIN, encoding: String = "binary") = apply {
            partBody(name, value.toRequestBody(mediaType), encoding)
        }

        fun partString(name: String, values: List<String>, mediaType: okhttp3.MediaType = HttpHeaders.MEDIA_TYPE_PLAIN, encoding: String = "binary") = apply {
            for (value in values) {
                partString(name, value, mediaType, encoding)
            }
        }

        fun partString(params: Map<String, String>) = apply {
            for ((name, value) in params) {
                partString(name, value)
            }
        }

        fun partFile(name: String, value: File, mediaType: okhttp3.MediaType? = null, encoding: String = "binary") = apply {
            partBody(name, value.asRequestBody(mediaType ?: Utils.guessMimeType(value.name)), encoding)
        }

        fun partFile(params: Map<String, File>) = apply {
            for ((name, value) in params) {
                partFile(name, value)
            }
        }

        fun partByteArray(params: Map<String, ByteArray>) = apply {
            for ((name, value) in params) {
                partByteArray(name, value)
            }
        }

        fun partByteArray(name: String, value: ByteArray, contentType: okhttp3.MediaType = HttpHeaders.MEDIA_TYPE_STREAM, encoding: String = "binary") = apply {
            partBody(name, value.toRequestBody(contentType), encoding)
        }

        fun partBody(name: String, value: okhttp3.RequestBody, encoding: String = "binary") = apply {
            partParams(PartField(name, value, encoding))
        }

        fun partParams(partField: PartField) = apply {
            if (!this.gotPart) {
                this.gotPart = true
            }
            this.partParams.add(partField)
        }

        /*上传一个文件*/
        fun requestBody(value: File, contentType: okhttp3.MediaType? = null) = apply {
            requestBody(value.asRequestBody(contentType ?: Utils.guessMimeType(value.name)))
        }

        /*上传字符串数据*/
        fun requestBody(value: String, contentType: okhttp3.MediaType = HttpHeaders.MEDIA_TYPE_PLAIN) = apply {
            requestBody(value.toRequestBody(contentType))
        }

        /*上传Json字符串数据*/
        fun jsonRequestBody(value: String) = apply {
            requestBody(value, HttpHeaders.MEDIA_TYPE_JSON)
        }

        /*上传字节数组*/
        fun requestBody(value: ByteArray, contentType: okhttp3.MediaType = HttpHeaders.MEDIA_TYPE_STREAM) = apply {
            requestBody(value.toRequestBody(contentType))
        }

        inline fun <reified T : Any> requestBody(value: T) = apply {
            val requestBodyConverter = net.requestBodyConverter<T>(object : TypeRef<T>() {}.type)
            requestBody(requestBodyConverter.convert(value))
        }

        fun requestBody(requestBody: okhttp3.RequestBody?) = apply {
            if (isFormEncoded || isMultipart) {
                throw IllegalStateException("Body parameter cannot be used with form or multi-part encoding.")
            }
            if (this.gotBody) {
                throw IllegalStateException("Body parameter has been set.")
            }
            this.gotBody = true
            this.requestBody = requestBody
        }

        fun tag(tag: Any) = apply {
            this.mTag = tag
        }

        /**
         * 通常，我们在下载的时候会使用Streaming修饰，
         * 因为这样不会将接口返回的数据全部加载到内存中，而是和服务器建立了一个连接，我们可以通过ResponseBody获取到流，然后对其进行读写。
         * 若是不用@Streaming修饰的话，正常的一次请求会将服务器返回的内容缓存到内存中，然后再返回。
         *
         *
         * 下载文件时调用
         */
        fun streaming() = apply {
            this.isStreaming = true
        }

        /*需要在这个请求里面添加通用headers*/
        fun needCommonHeaders(needCommonHeaders: Boolean) = apply {
            this.needCommonHeaders = needCommonHeaders
        }

        /*需要在这个请求里面添加通用params*/
        fun needCommonParams(needCommonParams: Boolean) = apply {
            this.needCommonParams = needCommonParams
        }

        fun build(): RequestFactory {
            if (!hasBody) {
                require(!isMultipart) { "Multipart can only be specified on HTTP methods with request body" }
                require(!isFormEncoded) { "isFormEncoded can only be specified on HTTP methods with request body" }
            }

            if (urlPath.isNullOrBlank() || !gotUrl) {
                throw IllegalArgumentException("urlPath must not be null")
            }

            if (!isFormEncoded && !isMultipart && !hasBody && gotBody) {
                throw IllegalArgumentException("Non-body HTTP method cannot contain requestBody:$requestBody")
            }

            if (isFormEncoded && !gotField) {
                throw IllegalArgumentException("Form-encoded method must contain at least one FieldParam")
            }

            if (isMultipart && !gotPart) {
                throw IllegalArgumentException("Form-encoded method must contain at least one FieldParam")
            }

            return RequestFactory(this)
        }

        inline fun <reified T : Any> parseClazz(): Call<T> {
            return build().parseClazz<T>()
        }

        inline fun <reified T : Any, reified R : Any> parseObject(): R {
            return build().parseClazz<T, R>(object : TypeRef<R>() {})
        }

    }
}