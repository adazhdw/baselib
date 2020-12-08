package com.adazhdw.kthttp.callback

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.kthttp.request.CallProxy
import com.adazhdw.kthttp.util.HttpLifecycleObserver
import com.adazhdw.ktlib.core.KtExecutors
import com.google.gson.JsonParseException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * author：daguozhu
 * date-time：2020/11/17 20:25
 * description：
 **/

abstract class OkHttpCallback(
    val mCallProxy: CallProxy,
    val mLifecycleOwner: LifecycleOwner?
) : Callback {

    init {
        HttpLifecycleObserver.bind(mLifecycleOwner, onDestroy = { mCallProxy.cancel() })
    }

    override fun onResponse(call: Call, response: Response) {
        try {
            KtExecutors.networkIO.submit {
                response.use { response(it) }
            }
        } catch (e: JsonParseException) {
            failure(e, call)
        } catch (e: Exception) {
            failure(e, call)
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        failure(e, call)
    }

    abstract fun response(response: Response)

    abstract fun failure(e: Exception, call: Call)
}
