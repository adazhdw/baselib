package com.adazhdw.net

import okhttp3.Protocol
import okhttp3.Request

/** An HTTP response. */
class Response<T>(
    val rawResponse: okhttp3.Response,
    val body: T?,
    val errorBody: okhttp3.ResponseBody?
) {
    companion object {
        /** Create a synthetic successful response with {@code body} as the deserialized body. */
        @JvmStatic
        fun <T> success(body: T): Response<T> {
            return success(
                body, okhttp3.Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        /**
         * Create a synthetic successful response with an HTTP status code of {@code code} and {@code
         * body} as the deserialized body.
         */
        @JvmStatic
        fun <T> success(code: Int, body: T): Response<T> {
            if (code < 200 || code >= 300) {
                throw IllegalArgumentException("code < 200 or code >= 300: $code")
            }
            return success(
                body, okhttp3.Response.Builder()
                    .code(code)
                    .message("Response.success()")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        /**
         * Create a synthetic successful response using {@code headers} with {@code body} as the
         * deserialized body.
         */
        @JvmStatic
        fun <T> success(body: T, headers: okhttp3.Headers): Response<T> {
            return success(
                body, okhttp3.Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .headers(headers)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        /**
         * Create a successful response from {@code rawResponse} with {@code body} as the deserialized
         * body.
         */
        @JvmStatic
        fun <T> success(body: T?, rawResponse: okhttp3.Response): Response<T> {
            if (!rawResponse.isSuccessful) {
                throw IllegalArgumentException("rawResponse must be successful response")
            }
            return Response<T>(rawResponse, body, null)
        }

        /** Create an error response from {@code rawResponse} with {@code body} as the error body. */
        @JvmStatic
        fun <T> error(body: okhttp3.ResponseBody, rawResponse: okhttp3.Response): Response<T> {
            if (rawResponse.isSuccessful) {
                throw IllegalArgumentException("rawResponse should not be successful response")
            }
            return Response<T>(rawResponse, null, body)
        }
    }

    /** HTTP status code. */
    val code: Int = rawResponse.code

    /** HTTP status message or null if unknown. */
    val message: String = rawResponse.message

    /** HTTP headers. */
    fun headers(): okhttp3.Headers = rawResponse.headers

    /** Returns true if {@link #code()} is in the range [200..300). */
    val isSuccessful: Boolean = rawResponse.isSuccessful

    override fun toString(): String {
        return rawResponse.toString()
    }
}