package com.adazhdw.ktlib.kthttp.converter

import com.adazhdw.ktlib.kthttp.exception.ExceptionHelper
import com.adazhdw.ktlib.kthttp.model.KtConfig
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.Type

/**
 * author：daguozhu
 * date-time：2020/11/18 11:18
 * description：
 **/

@Throws(IOException::class)
fun <R> Response.convert(type: Type): R {
    val body = ExceptionHelper.getNotNullResult(this).string()
    val needEncoder = KtConfig.needDecodeResult
    return KtConfig.converter.convert(body, type, needEncoder)
}