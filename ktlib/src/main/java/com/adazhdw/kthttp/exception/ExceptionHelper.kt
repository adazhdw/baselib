package com.adazhdw.kthttp.exception

import com.adazhdw.kthttp.constant.HttpConstant
import com.adazhdw.ktlib.utils.NetworkUtil
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * author：daguozhu
 * date-time：2020/11/6 15:09
 * description：
 **/
object ExceptionHelper {

    @Throws(IOException::class)
    fun getNotNullResponseBody(response: Response): ResponseBody {
        val body = response.body ?: throw HttpStatusException(response)
        if (!response.isSuccessful) throw HttpStatusException(response)
        return body
    }

    fun callError(e: Exception): NetException {
        return when {
            e is SocketTimeoutException -> RequestTimeoutException()
            e is InterruptedIOException && e.message == "timeout" -> RequestTimeoutException()
            e is UnknownHostException && !NetworkUtil.isConnected() -> NetWorkUnAvailableException()
            e is IOException && e.message == "Canceled" -> RequestCanceledException()
            else -> NetException(
                HttpConstant.ERROR_REQUEST_ON_FAILURE,
                e.message ?: "request error"
            )
        }
    }
}