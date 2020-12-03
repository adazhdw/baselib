package com.adazhdw.ktlib.base.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class CoroutinesFragment : Fragment() {

    protected val TAG = this.javaClass.simpleName + "-${this.hashCode()}-"

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