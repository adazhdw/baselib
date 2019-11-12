package com.adazhdw.ktlib.core

import android.content.res.Resources
import android.os.Looper

/**
 * author: daguozhu
 * created on: 2019/10/22 18:54
 * description:
 */

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()


/**
 * @return the status bar's height
 */
val statusBarHeight: Int
    get() {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
