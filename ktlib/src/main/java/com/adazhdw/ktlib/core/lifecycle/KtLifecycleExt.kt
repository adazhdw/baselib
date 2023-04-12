package com.adazhdw.ktlib.core.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


inline fun LifecycleOwner.addOnDestroy(crossinline action: (() -> Unit)) {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            action.invoke()
            lifecycle.removeObserver(this)
        }
    })
}