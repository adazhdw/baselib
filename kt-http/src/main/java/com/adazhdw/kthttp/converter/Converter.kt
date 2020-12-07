package com.adazhdw.kthttp.converter

import com.adazhdw.kthttp.KtConfig
import com.adazhdw.kthttp.exception.ExceptionHelper
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
    val body = ExceptionHelper.getNotNullResponseBody(this).string()
    val needEncoder = KtConfig.needDecodeResult
    return KtConfig.converter.convert(body, type, needEncoder)
}