@file:Suppress("NOTHING_TO_INLINE","unused")

package com.adazhdw.ktlib.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import java.util.regex.Pattern

/**
 * 删除所有的标点符号
 *
 */
fun String.trimPunct(): String {
    return if (this.isEmpty()) {
        ""
    } else this.replace("[\\pP\\p{Punct}]".toRegex(), "")
}

fun String.formatNum():String{
    val regEx = "[^0-9]"
    val p = Pattern.compile(regEx)
    val m = p.matcher(this)
    return m.replaceAll("").trim()
}

/**
 * 检查汉字
 */
fun String.checkChinese(): Boolean {
    val regex = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    return matcher.find()
}

/**
 * 检测字符串中只能包含：中文、数字、下划线(_)、横线(-)
 */
fun String.checkNickname(): Boolean {
    val regex = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    return !matcher.find()
}

/**
 * 复制
 */
fun Context.copyText(content: String) {
    val cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val myClip = ClipData.newPlainText("text", content)
    cmb?.setPrimaryClip(myClip)
}

/**
 * 粘贴
 */
fun Context.pasteText(): String? {
    val cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val abc = cmb?.primaryClip
    return if (abc?.getItemAt(0) != null) {
        val item = abc.getItemAt(0)
        item.text.toString()
    } else {
        null
    }
}

/**
 * startWith
 */
inline fun String.startWidth(prefix: String, ignoreCase: Boolean = true): Boolean {
    return startsWith(prefix, ignoreCase)
}

