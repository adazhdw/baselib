package com.adazhdw.ktlib.kthttp.request

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okio.Timeout

/**
 * author：daguozhu
 * date-time：2020/11/17 19:50
 * description：
 **/
class RequestCallProxy(val call: Call) : Call {

    override fun request(): Request {
        return call.request()
    }

    override fun enqueue(responseCallback: Callback) {
        call.enqueue(responseCallback)
    }

    override fun execute(): Response {
        return call.execute()
    }

    override fun cancel() {
        call.cancel()
    }

    override fun clone(): Call {
        return call.clone()
    }

    override fun isCanceled(): Boolean {
        return call.isCanceled()
    }

    override fun isExecuted(): Boolean {
        return call.isExecuted()
    }

    override fun timeout(): Timeout {
        return call.timeout()
    }
}