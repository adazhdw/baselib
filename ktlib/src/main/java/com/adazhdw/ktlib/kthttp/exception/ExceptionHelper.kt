package com.adazhdw.ktlib.kthttp.exception

import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * author：daguozhu
 * date-time：2020/11/6 15:09
 * description：
 **/
object ExceptionHelper {

    @Throws(IOException::class)
    fun getNotNullResult(response: Response): ResponseBody {
        val body = response.body ?: throw HttpStatusException(response)
        if (!response.isSuccessful) throw HttpStatusException(response)
        return body
    }
}