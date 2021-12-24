package com.adazhdw.net

import java.util.*

/** Exception for an unexpected, non-2xx HTTP response.  */
class HttpException(@Transient private val response: Response<*>) : RuntimeException(getMessage(response)) {
    private val code: Int = response.code
    override val message: String = response.message

    /** HTTP status code.  */
    fun code(): Int {
        return code
    }

    /** HTTP status message.  */
    fun message(): String {
        return message
    }

    /** The full HTTP response. This may be null if the exception was serialized.  */
    fun response(): Response<*> {
        return response
    }

    companion object {
        private fun getMessage(response: Response<*>?): String {
            Objects.requireNonNull(response, "response == null")
            return "HTTP " + response?.code.toString() + " " + response?.message
        }
    }

}