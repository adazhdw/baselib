package com.adazhdw.libapp.net

import com.adazhdw.net.Converter
import com.adazhdw.net.Net
import com.adazhdw.net.RequestFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import java.lang.reflect.Type

class GsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    override fun responseBodyConverter(type: Type, net: Net, requestFactory: RequestFactory): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type, net: Net): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson, adapter)
    }

    override fun responseConverter(type: Type, net: Net, requestFactory: RequestFactory): Converter<Response, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonResponseConverter(gson, adapter)
    }

    companion object {
        @JvmOverloads
        fun create(gson: Gson? = Gson()): GsonConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return GsonConverterFactory(gson)
        }
    }
}