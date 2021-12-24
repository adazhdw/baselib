package com.adazhdw.net

import java.lang.reflect.Type


abstract class InternalAdapters<ResponseT, ReturnT>(
    private val requestFactory: RequestFactory,
    private val callFactory: okhttp3.Call.Factory,
    private val responseConverter: Converter<okhttp3.Response, ResponseT>?,
    private val responseBodyConverter: Converter<okhttp3.ResponseBody, ResponseT>
) {

    companion object {

        fun <ResponseT, ReturnT> parse(returnType: Type, net: Net, requestFactory: RequestFactory): ReturnT {
            val callAdapter = createCallAdapter<ResponseT, ReturnT>(net, returnType)
            val responseType = callAdapter.responseType()
            if (responseType === okhttp3.Response::class.java) {
                throw IllegalArgumentException("$responseType is not a valid response body type")
            }
            if (responseType === Response::class.java) {
                throw IllegalArgumentException("Response must include generic type (e.g., Response<String>)")
            }
            if (requestFactory.method is HttpMethod.HEAD && (Void::class.java != responseType || Unit::class.java != responseType)) {
                throw IllegalArgumentException("HEAD method must use Void as response type.")
            }

            val responseBodyConverter = createResponseBodyConverter<ResponseT>(net, responseType, requestFactory)
            val responseConverter = createResponseConverter<ResponseT>(net, responseType, requestFactory)

            val callFactory = net.client

            return CallAdapted<ResponseT, ReturnT>(requestFactory, callFactory, responseConverter, responseBodyConverter, callAdapter).internalAdapt()
        }

        @JvmStatic
        private fun <ResponseT> createResponseBodyConverter(net: Net, responseType: Type, requestFactory: RequestFactory): Converter<okhttp3.ResponseBody, ResponseT> {
            try {
                return net.responseBodyConverter<ResponseT>(responseType, requestFactory)
            } catch (e: RuntimeException) {
                throw IllegalArgumentException("Unable to create converter for responseType:$responseType")
            }
        }

        @JvmStatic
        private fun <ResponseT> createResponseConverter(net: Net, responseType: Type, requestFactory: RequestFactory): Converter<okhttp3.Response, ResponseT>? {
            try {
                return net.responseConverter<ResponseT>(responseType, requestFactory)
            } catch (e: RuntimeException) {
                throw IllegalArgumentException("Unable to create converter for responseType:$responseType")
            }
        }

        @JvmStatic
        fun <ResponseT, ReturnT> createCallAdapter(net: Net, returnType: Type): CallAdapter<ResponseT, ReturnT> {
            try {
                return net.callAdapter(returnType) as CallAdapter<ResponseT, ReturnT>
            } catch (e: RuntimeException) {
                throw IllegalArgumentException("Unable to create call adapter for returnType:$returnType")
            }
        }

    }

    internal fun internalAdapt(): ReturnT {
        val call: Call<ResponseT> = OkHttpCall<ResponseT>(requestFactory, callFactory, responseConverter, responseBodyConverter)
        return adapt(call)
    }

    abstract fun adapt(call: Call<ResponseT>): ReturnT

    class CallAdapted<ResponseT, ReturnT>(
        requestFactory: RequestFactory,
        callFactory: okhttp3.Call.Factory,
        responseConverter: Converter<okhttp3.Response, ResponseT>?,
        responseBodyConverter: Converter<okhttp3.ResponseBody, ResponseT>,
        private val callAdapter: CallAdapter<ResponseT, ReturnT>
    ) : InternalAdapters<ResponseT, ReturnT>(requestFactory, callFactory, responseConverter, responseBodyConverter) {
        override fun adapt(call: Call<ResponseT>): ReturnT {
            return callAdapter.adapt(call)
        }
    }

}