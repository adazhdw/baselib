package com.adazhdw.ktlib.kthttp.callback

import com.adazhdw.ktlib.kthttp.converter.IConverter
import com.adazhdw.ktlib.kthttp.converter.JsonConverter
import okhttp3.Response

/**
 * author：daguozhu
 * date-time：2020/9/5 17:14
 * description：
 **/
abstract class RequestJsonCallback<T : Any>(private val clazz: Class<T>) : RequestCallbackImpl(),
    IConverter<T> {

    override fun onResponse(httpResponse: Response, response: String?) {
        onSuccess(convertResponse(httpResponse))
    }

    abstract fun onSuccess(data: T)
    abstract fun onError(code: Int, msg: String?)

    override fun onFailure(e: Exception, code: Int, msg: String?) {
        onError(code, msg)
    }

    override fun convertResponse(response: Response): T {
        val convert: JsonConverter<T> = JsonConverter(clazz)
        return convert.convertResponse(response)
    }
}