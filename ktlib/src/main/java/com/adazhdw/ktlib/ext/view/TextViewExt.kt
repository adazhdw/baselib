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

fun TextView.setTextSizeDp(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
}

fun TextView.setTextSizeSp(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}

fun TextView.setTextSizePx(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
}

fun TextView.textBold() {
    typeface = Typeface.DEFAULT_BOLD
}

fun TextView.textDefault() {
    typeface = Typeface.DEFAULT
}

/**
 * 获取文本
 */
fun TextView.textString(): String {
    return this.text.toString()
}

/**
 * 获取去除空字符串的文本
 */
fun TextView.textStringTrim(): String {
    return this.textString().trim()
}

/**
 * 文本是否为空
 */
fun TextView.isEmpty(): Boolean {
    return this.textString().isEmpty()
}

/**
 * 去空字符串后文本是否为空
 */
fun TextView.isTrimEmpty(): Boolean {
    return this.textStringTrim().isEmpty()
}

fun TextView.drawableLeft(@DrawableRes id: Int) {
    val d = context.getDrawableEx(id) ?: return
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(d, null, null, null)
}

fun TextView.drawableBottom(@DrawableRes id: Int) {
    val d = context.getDrawableEx(id) ?: return
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(null, null, null, d)
}

fun TextView.drawableRight(@DrawableRes id: Int) {
    val d = context.getDrawableEx(id) ?: return
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(null, null, d, null)
}

fun TextView.drawableTop(@DrawableRes id: Int) {
    val d = context.getDrawableEx(id) ?: return
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(null, d, null, null)
}

