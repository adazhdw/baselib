package com.adazhdw.ktlib.ext

import android.content.Context
import android.content.res.Configuration

/**
 * author: daguozhu
 * created on: 2019/11/8 10:48
 * description:
 */

inline val Context.displayMetrics: android.util.DisplayMetrics
    get() = resources.displayMetrics

inline val Context.configuration: Configuration
    get() = resources.configuration

inline val Configuration.portrait: Boolean
    get() = orientation == Configuration.ORIENTATION_PORTRAIT

inline val Configuration.landscape: Boolean
    get() = orientation == Configuration.ORIENTATION_LANDSCAPE
