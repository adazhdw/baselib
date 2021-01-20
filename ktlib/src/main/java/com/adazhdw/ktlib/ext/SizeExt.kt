package com.adazhdw.ktlib.ext

import android.content.res.Resources
import android.util.TypedValue
import com.adazhdw.ktlib.KtLib


fun dp2px(dpValue: Float): Int {
    val scale = KtLib.getApp().resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun px2dp(pxValue: Float): Int {
    val scale = KtLib.getApp().resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun sp2px(dpValue: Float): Int {
    val fontScale = KtLib.getApp().resources.displayMetrics.scaledDensity
    return (dpValue * fontScale + 0.5f).toInt()
}

fun px2sp(pxValue: Float): Int {
    val fontScale = KtLib.getApp().resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

/**
 * 在非 xml 环境下构建布局，需要将 px 转换为 dp 来进行多屏幕适配。
 */
val Int.dp: Int
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

val Int.sp: Int
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

