package com.adazhdw.net

import okhttp3.RequestBody
import java.io.IOException
import java.lang.reflect.Type

/**
 * author：daguozhu
 * date-time：2020/9/5 16:13
 * description：
 **/
interface Converter<F, T> {
    @Throws(IOException::class)
    fun convert(value: F): T?

    abstract class Factory {

        /**
         * Returns a {@link Converter} for converting an HTTP response body to {@code type}, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for
         * response types such as {@code SimpleResponse} from a {@code Call<SimpleResponse>}
         * declaration.
         */
        open fun responseBodyConverter(type: Type, net: Net, requestFactory: RequestFactory): Converter<okhttp3.ResponseBody, *>? {
            return null
        }

        /**
         * Returns a {@link Converter} for converting an HTTP response to {@code type}, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for
         * response types such as {@code SimpleResponse} from a {@code Call<SimpleResponse>}
         * declaration.
         */
        open fun responseConverter(type: Type, net: Net, requestFactory: RequestFactory): Converter<okhttp3.Response, *>? {
            return null
        }

        /**
         * Returns a {@link Converter} for converting {@code type} to an HTTP request body, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for types
         * specified by {@link Body @Body}, {@link Part @Part}, and {@link PartMap @PartMap} values.
         */
        open fun requestBodyConverter(type: Type, net: Net): Converter<*, RequestBody>? {
            return null
        }

    }
}