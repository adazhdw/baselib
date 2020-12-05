package com.adazhdw.ktlib.ext

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * author: daguozhu
 * created on: 2019/11/8 10:48
 * description:
 */

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

fun Context.getDrawableEx(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}
