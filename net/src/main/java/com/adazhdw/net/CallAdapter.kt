package com.adazhdw.net

import com.adazhdw.net.CallAdapter.Factory
import java.lang.reflect.Type

/**
 * Adapts a [Call] with response type `R` into the type of `T`. Instances are
 * created by [a factory][Factory] which is []
 */
interface CallAdapter<R, T> {
    /**
     * Returns the value type that this adapter uses when converting the HTTP response body to a Java
     * object. For example, the response type for `Call<Repo>` is `Repo`. This type is
     * used to prepare the `call` passed to `#adapt`.
     */
    fun responseType(): Type

    /**
     * Returns an instance of `T` which delegates to `call`.
     *
     * For example, given an instance for a hypothetical utility, `Async`, this instance
     * would return a new `Async<R>` which invoked `call` when run.
     */
    fun adapt(call: Call<R>): T

    /**
     * Creates [CallAdapter] instances based on the return type
     */
    abstract class Factory {
        /**
         * Returns a call adapter for interface methods that return `returnType`, or null if it
         * cannot be handled by this factory.
         */
        abstract fun get(returnType: Type, net: Net): CallAdapter<*, *>?

    }
}