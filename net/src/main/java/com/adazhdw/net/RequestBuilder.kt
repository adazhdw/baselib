package com.adazhdw.net

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import java.util.regex.Pattern

internal class RequestBuilder(
    private val method: String,
    private val baseUrl: okhttp3.HttpUrl,
    private var urlPath: String?,
    private val headers: okhttp3.Headers?,
    private var contentType: okhttp3.MediaType?,
    private val hasBody: Boolean,
    isFormEncoded: Boolean,
    isMultipart: Boolean
) {

    private val requestBuilder: okhttp3.Request.Builder by lazy { okhttp3.Request.Builder() }
    private val headersBuilder: okhttp3.Headers.Builder by lazy { headers?.newBuilder() ?: okhttp3.Headers.Builder() }
    private var urlBuilder: okhttp3.HttpUrl.Builder? = null
    private var multipartBuilder: okhttp3.MultipartBody.Builder? = null
    private var formBuilder: okhttp3.FormBody.Builder? = null
    private var body: okhttp3.RequestBody? = null

    init {
        if (isFormEncoded) {
            formBuilder = okhttp3.FormBody.Builder()
        } else if (isMultipart) {
            multipartBuilder = okhttp3.MultipartBody.Builder().setType(okhttp3.MultipartBody.FORM)
        }
    }

    internal fun setUrlPath(urlPath: Any) {
        this.urlPath = urlPath.toString()
    }

    internal fun addHeader(name: String, value: String) {
        if (HttpHeaders.KEY_CONTENT_TYPE.equals(name, true)) {
            try {
                contentType = value.toMediaType()
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Malformed content type: $value", e)
            }
        } else {
            headersBuilder.add(name, value)
        }
    }

    internal fun addPathParam(name: String, value: String) {
        urlPath?.let { url ->
            val replacement: String = canonicalizeForPath(value)
            val newUrlPath: String = url.replace("{$name}", replacement)
            if (PATH_TRAVERSAL.matcher(newUrlPath).matches()) {
                throw IllegalArgumentException("@Path parameters shouldn't perform path traversal ('.' or '..'): $value")
            }
            this.urlPath = newUrlPath
        } ?: throw AssertionError()
    }

    private fun canonicalizeForPath(input: String): String {
        return input
    }

    internal fun addQueryParam(name: String, value: String, encoded: Boolean) {
        urlPath?.let { url ->
            urlBuilder = baseUrl.newBuilder(url)
            if (urlBuilder == null) {
                throw IllegalArgumentException("Malformed URL. Base: $baseUrl, Relative: $urlPath")
            }
            urlPath = null
        }

        if (encoded) {
            urlBuilder?.addEncodedQueryParameter(name, value)
        } else {
            urlBuilder?.addQueryParameter(name, value)
        }
    }

    // Only called when isFormEncoded was true.
    internal fun addFormField(name: String, value: String, encoded: Boolean) {
        if (encoded) {
            formBuilder?.addEncoded(name, value)
        } else {
            formBuilder?.add(name, value)
        }
    }

    // Only called when isMultipart was true.
    /** Add a part to the body. */
    internal fun addPart(headers: okhttp3.Headers, body: okhttp3.RequestBody) = apply {
        multipartBuilder?.addPart(headers, body)
    }

    internal fun addPart(part: okhttp3.MultipartBody.Part) {
        multipartBuilder?.addPart(part)
    }

    internal fun setBody(body: okhttp3.RequestBody) {
        this.body = body
    }

    internal fun addTag(tag: Any?) {
        requestBuilder.tag(tag)
    }

    internal fun get(): okhttp3.Request.Builder {
        val url: okhttp3.HttpUrl?
        val urlBuilder: okhttp3.HttpUrl.Builder? = this.urlBuilder
        if (urlBuilder != null) {
            url = urlBuilder.build()
        } else {
            url = baseUrl.resolve(urlPath ?: "")
            if (url == null) {
                throw IllegalArgumentException("Malformed URL. baseUrl: $baseUrl, urlPath: $urlPath")
            }
        }

        var body = this.body
        if (body == null) {
            // Try to pull from one of the builders.
            formBuilder?.run {
                body = this.build()
            } ?: multipartBuilder?.run {
                body = this.build()
            } ?: if (hasBody) {
                // Body is absent, make an empty body.
                body = byteArrayOf().toRequestBody()
            }
        }

        this.contentType?.let { contentType ->
            if (body != null) {
                body = ContentTypeOverridingRequestBody(body!!, contentType)
            } else {
                headersBuilder.add(HttpHeaders.KEY_CONTENT_TYPE, contentType.toString())
            }
        }

        return requestBuilder.url(url).headers(headersBuilder.build()).method(method, body)
    }

    private class ContentTypeOverridingRequestBody(
        private val delegate: okhttp3.RequestBody,
        private val contentType: okhttp3.MediaType?
    ) : okhttp3.RequestBody() {

        override fun contentLength(): Long {
            return delegate.contentLength()
        }

        override fun contentType(): okhttp3.MediaType? {
            return contentType
        }

        override fun writeTo(sink: BufferedSink) {
            delegate.writeTo(sink)
        }

    }


    companion object {

        internal val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        internal const val PATH_SEGMENT_ALWAYS_ENCODE_SET = " \"<>^`{}|\\?#"

        /**
         * Matches strings that contain `.` or `..` as a complete path segment. This also
         * matches dots in their percent-encoded form, `%2E`.
         *
         *
         * It is okay to have these strings within a larger path segment (like `a..z` or `index.html`) but when alone they have a special meaning. A single dot resolves to no path
         * segment so `/one/./three/` becomes `/one/three/`. A double-dot pops the preceding
         * directory, so `/one/../three/` becomes `/three/`.
         *
         *
         * We forbid these in Retrofit paths because they're likely to have the unintended effect. For
         * example, passing `..` to `DELETE /account/book/{isbn}/` yields `DELETE
         * /account/`.
         */
        internal val PATH_TRAVERSAL = Pattern.compile("(.*/)?(\\.|%2e|%2E){1,2}(/.*)?")

    }
}