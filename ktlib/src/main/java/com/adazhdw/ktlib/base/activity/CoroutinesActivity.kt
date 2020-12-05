package com.adazhdw.ktlib.base.activity

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// version 1.0
abstract class CoroutinesActivity : AppCompatActivity() {

    protected val TAG = javaClass.simpleName + "-${this.hashCode()}-"
    protected val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    fun launch(
        error: ((Throwable) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) = launchOnUI(error, block)

    protected fun launchOnUI(
        error: ((Throwable) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        lifecycleScope.launch {
            runCatching {
                block()
            }.onSuccess {
                "success".logD(TAG)
            }.onFailure {
                error?.invoke(it)
                "error:${it.message}".logE(TAG)
            }
        }
    }
}