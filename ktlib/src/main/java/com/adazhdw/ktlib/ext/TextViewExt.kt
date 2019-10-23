package com.adazhdw.ktlib.ext

import android.util.TypedValue
import android.widget.TextView

/**
 * author: daguozhu
 * created on: 2019/10/22 16:44
 * description:
 */

fun TextView.setTextSizeDp(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
}

fun TextView.setTextSizeSp(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}

fun TextView.setTextSizePx(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
}