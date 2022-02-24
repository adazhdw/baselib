package com.adazhdw.ktlib.ext

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding


/**
 * ViewBinding Extension function
 */


inline fun <reified T : ViewBinding> viewBinding(layoutInflater: LayoutInflater): T {
    return T::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as T
}

inline fun <reified T : ViewBinding> Activity.viewBind() = lazy {
    viewBinding<T>(layoutInflater).apply { setContentView(this.root) }
}