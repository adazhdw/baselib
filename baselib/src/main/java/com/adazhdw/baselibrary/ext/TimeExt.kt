package com.adazhdw.baselibrary.ext

import android.icu.text.SimpleDateFormat
import android.os.Build
import java.util.*

val ymdFormat by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)
    } else {
        java.text.SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)
    }
}

val ymdFormat2 by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    } else {
        java.text.SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    }
}

val allFormat by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA)
    } else {
        java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA)
    }
}

val allFormat2 by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    } else {
        java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    }
}

val imgFormat by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
    } else {
        java.text.SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
    }
}

fun getYmdString(): String {
    return ymdFormat.format(Date(System.currentTimeMillis()))
}

fun getYmdString2(): String {
    return ymdFormat2.format(Date(System.currentTimeMillis()))
}

fun getAllString(): String {
    return allFormat.format(Date(System.currentTimeMillis()))
}

fun getAllString2(): String {
    return allFormat2.format(Date(System.currentTimeMillis()))
}

fun getImgFormat(): String {
    return imgFormat.format(Date(System.currentTimeMillis()))
}


private val mFormatBuilder: StringBuilder by lazy { StringBuilder() }
private val mFormatter: Formatter by lazy { Formatter(mFormatBuilder, Locale.CHINA) }
fun stringForTime(timeMs: Int): String {
    val totalSeconds = timeMs / 1000

    val seconds = totalSeconds % 60
    val minutes = totalSeconds / 60 % 60
    val hours = totalSeconds / 3600

    mFormatBuilder.setLength(0)
    return if (hours > 0) {
        mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
    } else {
        mFormatter.format("%02d:%02d", minutes, seconds).toString()
    }
}

