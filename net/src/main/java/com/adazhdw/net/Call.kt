package com.adazhdw.net

import okio.Timeout
import java.io.IOException

interface Call<T> : Cloneable {

    /**
     * Synchronously send the request and return its response.
     */
    @Throws(IOException::class)
    fun execute(): Response<T>

    /**
     * Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     */
    fun enqueue(callback: Callback<T>)

    /**
     * Returns true if this call has been either execute() or enqueue().
     * It is an error to execute or enqueue a call more than once.
     */
    fun isExecuted(): Boolean

    /**
     * Cancel this call. An attempt will be made to cancel in-flight calls, and if the call has not
     * yet been executed it never will be.
     */
    fun cancel()

    /** True if [.cancel] was called.  */
    fun isCanceled(): Boolean

    /**
     * Create a new, identical call to this one which can be enqueued or executed even if this call
     * has already been.
     */
    fun copy(): Call<T>

    /** The original HTTP request. */
    fun request(): okhttp3.Request

    fun getRawCall(): okhttp3.Call

    /**
     * Returns a timeout that spans the entire call: resolving DNS, connecting, writing the request
     * body, server processing, and reading the response body. If the call requires redirects or
     * retries all must complete within one timeout period.
     */
    fun timeout(): Timeout

}