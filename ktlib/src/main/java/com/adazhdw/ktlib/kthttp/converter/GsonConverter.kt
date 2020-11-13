package com.adazhdw.ktlib.kthttp.converter

import com.adazhdw.ktlib.kthttp.model.KtConfig
import com.adazhdw.ktlib.kthttp.util.GsonUtils
import com.google.gson.Gson
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
    override fun <T> convert(result: String, type: Type?, needDecodeResult: Boolean): T {
        var result2 = result
        if (needDecodeResult) {
            result2 = KtConfig.getCoder().decode(result2)
        }
        if (type === String::class.java) return result2 as T
        return gson.fromJson(result, type)
    }

}