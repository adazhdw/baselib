package com.adazhdw.baselibrary.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

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

    fun getWholeFormat(timeMs: Long): String {
        if (timeMs <= 0) return ""
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        return format.format(Date(timeMs))
    }

    fun getWholeFormatEx(timeMs: Long): String {
        if (timeMs <= 0) return ""
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(Date(timeMs))
    }

    fun getYmdFormat(timeMs: Long): String {
        if (timeMs <= 0) return ""
        val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return format.format(Date(timeMs))
    }

    fun getYmdFormatEx(timeMs: Long): String {
        if (timeMs <= 0) return ""
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(Date(timeMs))
    }

}
