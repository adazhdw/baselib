package com.adazhdw.ktlib.ext

import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi

/**
 * author: daguozhu
 * created on: 2019/10/21 17:00
 * description:
 */

/**
 * Set visibility Visible
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Set visibility invisible
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Set visibility gone
 */
fun View.gone() {
    visibility = View.GONE
}

var View.isVisible
    get() = visibility == View.VISIBLE
    set(value) = if (value) visible() else gone()

var View.isInvisible
    get() = visibility == View.INVISIBLE
    set(value) = if (value) invisible() else visible()

var View.isGone
    get() = visibility == View.GONE
    set(value) = if (value) gone() else visible()

/**
 * Causes the Runnable which contains action() to be added to the message queue, to be run
 * after the specified amount of time elapses.
 * The runnable will be run on the user interface thread
 *
 * @param action Will be invoked in the Runnable
 * @param delayMillis The delay (in milliseconds) until the action() will be invoked
 */
inline fun View.postDelayed(crossinline action: () -> Unit, delayMillis: Long = 1000): Runnable {
    val runnable = Runnable { action() }
    postDelayed(runnable, delayMillis)
    return runnable
}

/**
 * Register a callback to be invoked after the view is measured
 *
 * @param callback The callback() to be invoked
 */
inline fun View.afterMeasure(crossinline callback: View.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                callback()
            }
        }
    })
}

