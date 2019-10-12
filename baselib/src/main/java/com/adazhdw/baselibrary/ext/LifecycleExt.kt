package com.adazhdw.baselibrary.ext

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


inline fun FragmentActivity.addOnDestroy(crossinline action: (() -> Unit)) {
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            action.invoke()
        }
    })
}