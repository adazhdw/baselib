package com.adazhdw.ktlib.core.lifecycle

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * Description: 自动在UI销毁时移除msg和任务的Handler，不会有内存泄露
 */
class LifecycleHandler(
    private val lifecycleOwner: LifecycleOwner?,
    looper: Looper = Looper.getMainLooper()
) : Handler(looper), DefaultLifecycleObserver {
    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        removeCallbacksAndMessages(null)
        lifecycleOwner?.lifecycle?.removeObserver(this)
    }
}