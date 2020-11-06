package com.adazhdw.ktlib.kthttp.converter

import com.adazhdw.ktlib.kthttp.KtConfig
import com.adazhdw.ktlib.kthttp.util.GsonUtils
import com.google.gson.Gson
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * author：daguozhu
 * date-time：2020/11/3 19:05
 * description：
 **/
class GsonConverter private constructor(private val gson: Gson) : IConverter {

    companion object {
        fun create(): GsonConverter {
            return GsonConverter(GsonUtils.buildGson())
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> convertResponse(body: ResponseBody, type: Type, needDecodeResult: Boolean): T {
        body.use {
            var result = body.string()
            if (needDecodeResult) {
                result = KtConfig.coder.decode(result)
            }
            if (type === String::class.java) return result as T
            return gson.fromJson(result, type)
        }
    }

}