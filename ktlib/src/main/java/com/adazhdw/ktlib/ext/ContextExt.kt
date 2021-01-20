@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


fun Context.toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast.makeText(this, msg, duration).apply { show() }


fun Context.getColorEx(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}

fun Context.getDrawableEx(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.sp2px(dpValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (dpValue * fontScale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}


val Context.versionName: String
    get() = packageManager.getPackageInfo(packageName, 0).versionName

val Context.versionCode: Long
    get() = with(packageManager.getPackageInfo(packageName, 0)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) longVersionCode else versionCode.toLong()
    }

inline val Context.displayMetrics: android.util.DisplayMetrics
    get() = resources.displayMetrics

inline val Context.configuration: Configuration
    get() = resources.configuration

inline val Configuration.portrait: Boolean
    get() = orientation == Configuration.ORIENTATION_PORTRAIT

inline val Configuration.landscape: Boolean
    get() = orientation == Configuration.ORIENTATION_LANDSCAPE

inline val Context.portrait: Boolean
    get() = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

inline val Context.landscape: Boolean
    get() = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Context.screenWidth: Int
    get() {
        val wm: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display?.getRealSize(point)
        } else {
            wm.defaultDisplay.getRealSize(point)
        }// 小于17用：wm.defaultDisplay.getSize(point)
        return point.x
    }

val Context.screenHeight: Int
    get() {
        val wm: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            display?.getRealSize(point)
        } else {
            wm.defaultDisplay.getRealSize(point)
        }// 小于17用：wm.defaultDisplay.getSize(point)
        return point.y
    }


