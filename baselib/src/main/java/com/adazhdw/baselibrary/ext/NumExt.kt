package com.adazhdw.baselibrary.ext

import java.text.NumberFormat

fun percent(total: Float, current: Float): Int {
    return NumberFormat.getInstance().apply {
        maximumFractionDigits = 0
    }.format(current / total * 100).toInt()
}