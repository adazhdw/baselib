package com.adazhdw.ktlib.ext

import android.content.Context
import com.adazhdw.ktlib.LibUtil


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


fun dp2px(dpValue: Float): Int {
    val scale = LibUtil.getApp().resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun px2dp(pxValue: Float): Int {
    val scale = LibUtil.getApp().resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun sp2px(dpValue: Float): Int {
    val fontScale = LibUtil.getApp().resources.displayMetrics.scaledDensity
    return (dpValue * fontScale + 0.5f).toInt()
}

fun px2sp(pxValue: Float): Int {
    val fontScale = LibUtil.getApp().resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}