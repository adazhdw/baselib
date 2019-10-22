package com.adazhdw.ktlib.utils

import java.text.NumberFormat


class NumUtil {
    companion object {
        fun percent(total: Float, current: Float): Int {
            return NumberFormat.getInstance().apply {
                maximumFractionDigits = 0
            }.format(current / total * 100).toInt()
        }

        fun point2(total: Float, current: Float): String {
            return NumberFormat.getInstance().apply {
                maximumFractionDigits = 2
            }.format(current / total)
        }

        fun numPoint(total: Float, current: Float, point: Int = 2): String {
            return NumberFormat.getInstance().apply {
                maximumFractionDigits = point
            }.format(current / total)
        }
    }
}