package com.adazhdw.net

import androidx.annotation.GuardedBy
import com.adazhdw.net.Utils.Companion.throwIfFatal
import okhttp3.MediaType
import okhttp3.Request
import okio.*
import java.io.IOException

class OkHttpCall<T>(
    private val requestFactory: RequestFactory,
    private val callFactory: okhttp3.Call.Factory,
    private val responseConverter: Converter<okhttp3.Response, T>?,
    private val responseBodyConverter: Converter<okhttp3.ResponseBody, T>
) : Call<T> {

    @Volatile
    private var canceled = false

    @GuardedBy("this")
    private var rawCall: okhttp3.Call? = null

    @GuardedBy("this")
    private var creationFailure: Throwable? = null

    @GuardedBy("this")
    private var executed = false

    override fun copy(): Call<T> {
        return OkHttpCall<T>(requestFactory, callFactory, responseConverter, responseBodyConverter)
    }

    @Synchronized
    override fun request(): Request {
        try {
            return getRawCall().request()
        } catch (e: IOException) {
            throw RuntimeException("Unable to create request.", e)
        }
    }

    override fun timeout(): Timeout {
        try {
            return getRawCall().timeout()
        } catch (e: IOException) {
            throw RuntimeException("Unable to create request.", e)
        }
    }

    /**
     * Returns the raw call, initializing it if necessary. Throws if initializing the raw call throws,
     * or has thrown in previous attempts to create it.
     */
    @GuardedBy("this")
    override fun getRawCall(): okhttp3.Call {
        val call: okhttp3.Call? = rawCall
        if (call != null) return call

        // Re-throw previous failures if this isn't the first attempt.
        creationFailure?.let { creationFailure ->
            when (creationFailure) {
                is IOException -> {
                    throw creationFailure
                }
                is RuntimeException -> {
                    throw creationFailure
                }
                else -> {
                    throw creationFailure as Error
                }
            }
        }

        // Create and remember either the success or the failure.
        try {
            return createRawCall().also { rawCall = it }
        } catch (e: RuntimeException) {
            creationFailure = e
            throw e
        } catch (e: IOException) {
            creationFailure = e
            throw e
        } catch (e: Error) {
            throwIfFatal(e)// Do not assign a fatal error to creationFailure.
            creationFailure = e
            throw e
        }
    }

    private fun createRawCall(): okhttp3.Call {
        return callFactory.newCall(requestFactory.create())
    }

    override fun enqueue(callback: Callback<T>) {
        var call: okhttp3.Call?
        var failure: Throwable?

        synchronized(this) {
            if (executed) throw  IllegalStateException("Already executed")
            executed = true

            call = getRawCall()
            failure = creationFailure
            if (call == null && failure == null) {
                try {
                    call = createRawCall().also { rawCall = it }
                } catch (t: Throwable) {
                    throwIfFatal(t)
                    creationFailure = t
                    failure = creationFailure
                }
            }
        }

        if (failure != null) {
            callback.onFailure(this, failure!!)
            return
        }
        if (call == null) {
            callback.onFailure(this, IOException("Create okhttp3.Call failed"))
            return
        }
        if (canceled) {
            call?.cancel()
        }
        call?.enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val realResponse: Response<T>
                try {
                    realResponse = parseResponse(response)
                } catch (t: Throwable) {
                    throwIfFatal(t)
                    callFailure(t)
                    return
                }
                try {
                    callback.onResponse(this@OkHttpCall, realResponse)
                } catch (t: Throwable) {
                    throwIfFatal(t)
                    t.printStackTrace()
                }
            }

            private fun callFailure(t: Throwable) {
                try {
                    callback.onFailure(this@OkHttpCall, t)
                } catch (t: Throwable) {
                    throwIfFatal(t)
                    t.printStackTrace()
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callFailure(e)
            }

        })
    }

    override fun execute(): Response<T> {
        val call: okhttp3.Call
        synchronized(this) {
            if (executed) throw  IllegalStateException("Already executed")
            executed = true

            call = getRawCall()
        }

        if (canceled) {
            call.cancel()
        }

        return parseResponse(call.execute())
    }

    private fun parseResponse(rawResponse: okhttp3.Response): Response<T> {
        rawResponse.use { response ->
            val rawBody = response.body ?: throw IOException("okhttp3.Response'body is null")

            // Remove the body's source (the only stateful object) so we can pass the response along.
            val res = response.newBuilder()
                .body(NoContentResponseBody(rawBody.contentType(), rawBody.contentLength()))
                .build()

            if (response.isSuccessful) {
                val code = res.code
                if (code == 204 || code == 205) {
                    return Response.success<T>(null, response)
                }
                val catchingBody = ExceptionCatchingResponseBody(rawBody)
                if (responseConverter != null) {
                    try {
                        val body: T? = responseConverter.convert(response.newBuilder().body(catchingBody).build())
                        return Response.success(body, res)
                    } catch (e: IOException) {
                        throw e
                    }
                } else {
                    try {
                        val body: T? = responseBodyConverter.convert(catchingBody)
                        return Response.success(body, res)
                    } catch (e: RuntimeException) {
                        catchingBody.throwIfCaught()
                        throw e
                    }
                }
            } else {
                // Buffer the entire body to avoid future I/O.
                val bufferedBody = Utils.buffer(rawBody)
                return Response.error(bufferedBody, res)
            }
        }
    }

    @Synchronized
    override fun isExecuted(): Boolean {
        return executed
    }

    override fun cancel() {
        canceled = true

        val call: okhttp3.Call?
        synchronized(this) {
            call = rawCall
        }
        call?.cancel()
    }

    override fun isCanceled(): Boolean {
        if (canceled) {
            return true
        }
        synchronized(this) {
            return rawCall != null && rawCall!!.isCanceled()
        }
    }

    class NoContentResponseBody(
        private val contentType: MediaType?,
        private val contentLength: Long
    ) : okhttp3.ResponseBody() {
        override fun contentLength(): Long {
            return contentLength
        }

        override fun contentType(): MediaType? {
            return contentType
        }

        override fun source(): BufferedSource {
            throw IllegalStateException("Cannot read raw response body of a converted body.")
        }
    }

    class ExceptionCatchingResponseBody(private val delegate: okhttp3.ResponseBody) : okhttp3.ResponseBody() {

        private val delegateSource: BufferedSource
        private var throwException: IOException? = null

        init {
            delegateSource = object : ForwardingSource(delegate.source()) {
                override fun read(sink: Buffer, byteCount: Long): Long {
                    try {
                        return super.read(sink, byteCount)
                    } catch (e: IOException) {
                        throwException = e
                        throw e
                    }
                }
            }.buffer()
        }

        override fun contentLength(): Long {
            return delegate.contentLength()
        }

        override fun contentType(): MediaType? {
            return delegate.contentType()
        }

        override fun source(): BufferedSource {
            return delegateSource
        }

        override fun close() {
            delegate.close()
        }


        @Throws(IOException::class)
        fun throwIfCaught() {
            if (throwException != null) {
                throw throwException!!
            }
        }
    }
}