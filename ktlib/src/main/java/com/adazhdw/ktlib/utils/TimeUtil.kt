package com.adazhdw.ktlib.utils

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


    const val ymdFormat = "yyyy-MM-dd"
    const val ymdCNFormat = "yyyy年MM月dd日"
    const val ymdHmFormat = "yyyy-MM-dd HH:mm"
    const val allFormat = "yyyy-MM-dd HH:mm:ss"

    fun getFormatTime(date: Date, formatStr: String = allFormat): String {//可根据需要自行截取数据显示
        val format = SimpleDateFormat(formatStr, Locale.CHINA)
        return format.format(date)
    }

}
