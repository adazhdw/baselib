package com.adazhdw.baselibrary.ext

import android.content.Context
import com.adazhdw.baselibrary.LibUtil


fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.sp2px(dpValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (dpValue * fontScale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}