package com.adazhdw.ktlib.kthttp.callback

import androidx.lifecycle.LifecycleOwner
import com.adazhdw.ktlib.core.KtExecutors
import com.adazhdw.ktlib.kthttp.request.RequestCallProxy
import com.adazhdw.ktlib.kthttp.util.HttpLifecycleObserver
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
    val mCallProxy: RequestCallProxy,
    val mLifecycleOwner: LifecycleOwner?
) : Callback {

    init {
        HttpLifecycleObserver.bind(mLifecycleOwner, onDestroy = { mCallProxy.cancel() })
    }

    override fun onResponse(call: Call, response: Response) {
        try {
            KtExecutors.networkIO.submit {
                response.use { onResponse(it) }
            }
        } catch (e: Exception) {
            onFailure(e, call)
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        onFailure(e, call)
    }

    abstract fun onResponse(response: Response)

    abstract fun onFailure(e: Exception, call: Call)
}
