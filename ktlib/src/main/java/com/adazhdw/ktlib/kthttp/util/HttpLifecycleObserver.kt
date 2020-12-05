package com.adazhdw.ktlib.kthttp.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * author：daguozhu
 * date-time：2020/11/17 20:45
 * description：
 **/
class HttpLifecycleObserver(private val onDestroy: (() -> Unit)?) : LifecycleEventObserver {

    companion object {
        /**
         * 绑定组件的生命周期
         */
        fun bind(lifecycleOwner: LifecycleOwner?, onDestroy: (() -> Unit)? = null) {
            lifecycleOwner?.lifecycle?.addObserver(HttpLifecycleObserver(onDestroy))
        }

        /**
         * 判断宿主是否处于活动状态
         */
        fun isLifecycleActive(lifecycleOwner: LifecycleOwner?): Boolean {
            return lifecycleOwner != null && lifecycleOwner.lifecycle.currentState != Lifecycle.State.DESTROYED
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
            //cancel request
            onDestroy?.invoke()
        }
    }

}