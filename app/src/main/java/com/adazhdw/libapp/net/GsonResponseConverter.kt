package com.adazhdw.libapp.net

import com.adazhdw.net.Converter
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import okhttp3.Response
import java.io.IOException

internal class GsonResponseConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<Response, T> {
    @Throws(IOException::class)
    override fun convert(value: Response): T {
        return value.body?.use {
            val jsonReader = gson.newJsonReader(it.charStream())
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            result
        } ?: throw IOException("okhttp3.Response'body is null")
    }
}