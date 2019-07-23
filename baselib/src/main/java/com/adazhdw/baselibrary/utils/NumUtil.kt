package com.adazhdw.baselibrary.utils

import java.text.NumberFormat


class NumUtil {
    companion object{
        fun percent(total: Float, current: Float): Int {
            return NumberFormat.getInstance().apply {
                maximumFractionDigits = 0
            }.format(current / total * 100).toInt()
        }
    }
}