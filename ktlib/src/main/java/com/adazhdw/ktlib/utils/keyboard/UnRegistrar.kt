package com.adazhdw.ktlib.utils.keyboard

import android.view.ViewTreeObserver


interface UnRegistrar {

    /**
     * unregisters the [ViewTreeObserver.OnGlobalLayoutListener] and there by does provide any more callback to the [KeyboardVisibilityEventListener]
     */
    fun unregister()
}

