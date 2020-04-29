@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext.view

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.adazhdw.ktlib.ext.getDrawableEx

/**
 * author: daguozhu
 * created on: 2019/10/22 16:44
 * description:
 */

inline fun TextView.setTextSizeDp(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
}

inline fun TextView.setTextSizeSp(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}

inline fun TextView.setTextSizePx(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
}

inline fun TextView.textBold() {
    typeface = Typeface.DEFAULT_BOLD
}

inline fun TextView.textDefault() {
    typeface = Typeface.DEFAULT
}


inline fun TextView.drawableLeft(@DrawableRes id: Int) {
    val d = context.getDrawableEx(id) ?: return
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(d, null, null, null)
}

inline fun TextView.drawableBottom(@DrawableRes id: Int) {
    val d = context.getDrawableEx(id) ?: return
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(null, null, null, d)
}

inline fun TextView.drawableRight(@DrawableRes id: Int) {
    val d = context.getDrawableEx(id) ?: return
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(null, null, d, null)
}

inline fun TextView.drawableTop(@DrawableRes id: Int) {
    val d = context.getDrawableEx(id) ?: return
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(null, d, null, null)
}

