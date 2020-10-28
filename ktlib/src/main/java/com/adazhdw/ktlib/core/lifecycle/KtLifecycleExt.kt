package com.adazhdw.ktlib.core.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


inline fun LifecycleOwner.addOnDestroy(crossinline action: (() -> Unit)) {
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            action.invoke()
            lifecycle.removeObserver(this)
        }
    })
}