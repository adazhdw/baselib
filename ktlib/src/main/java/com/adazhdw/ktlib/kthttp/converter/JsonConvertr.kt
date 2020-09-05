package com.adazhdw.ktlib.kthttp.converter

import okhttp3.Response

/**
 * author：daguozhu
 * date-time：2020/9/5 17:55
 * description：
 **/
class JsonConverter<T : Any>(clazz: Class<T>) : IConverter<T> {
    override fun convertResponse(response: Response): T {

    }
}