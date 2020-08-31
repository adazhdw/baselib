package com.adazhdw.ktlib.kthttp.callback

import okhttp3.Headers
import okhttp3.Response

abstract class RequestCallback : BaseRequestCallback {


    override fun onStart() {

    }

    override fun onResponse(httpResponse: Response?, response: String?, headers: Headers) {

    }

    override fun onSuccess(result: String) {

    }

    override fun onError(code: Int, msg: String?) {

    }

    override fun onFinish() {

    }


}