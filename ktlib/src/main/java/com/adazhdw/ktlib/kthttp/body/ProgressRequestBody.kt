package com.adazhdw.ktlib.kthttp.body

import com.adazhdw.ktlib.kthttp.callback.ProgressCallback
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink

/**
 * 包装的请求体，处理请求进度
 */
class ProgressRequestBody(private val delegate: RequestBody, val callback: ProgressCallback?) :
    RequestBody() {
    override fun contentType(): MediaType? {
        return delegate.contentType()
    }

    override fun writeTo(sink: BufferedSink) {
        if (callback != null) {
            val buffer = Buffer()
            delegate.writeTo(sink)
            val size = buffer.size
            if (size == -1L) return

        } else {
            delegate.writeTo(sink)
        }
    }

}