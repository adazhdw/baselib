package com.adazhdw.ktlib.kthttp.exception

import com.adazhdw.ktlib.kthttp.model.HttpConstant
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
    fun getNotNullResult(response: Response): ResponseBody {
        val body = response.body ?: throw HttpStatusException(response)
        if (!response.isSuccessful) throw HttpStatusException(response)
        return body
    }

    fun callError(e: Exception): NetException {
        val ex: NetException /* = java.lang.Exception */
        var code = HttpConstant.ERROR_REQUEST_ON_FAILURE
        if (e is SocketTimeoutException) {
            ex = RequestTimeoutException(code)
        } else if (e is InterruptedIOException && e.message == "timeout") {
            ex = RequestTimeoutException(code)
        } else if (e is UnknownHostException && !NetworkUtil.isConnected()) {
            code = HttpConstant.ERROR_NETWORK_UNAVAILABLE
            ex = NetWorkUnAvailableException(code)
        } else if (e is IOException && e.message == "Canceled") {
            code = HttpConstant.ERROR_REQUEST_CANCEL_ERROR
            ex = RequestCanceledException(code)
        } else {
            ex = NetException(code, e.message ?: "request error")
        }
        return ex
    }
}