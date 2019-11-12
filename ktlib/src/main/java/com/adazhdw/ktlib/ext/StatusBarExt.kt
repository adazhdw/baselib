package com.adazhdw.ktlib.ext

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.adazhdw.ktlib.core.statusBarHeight

private const val TAG_STATUS_BAR = "TAG_STATUS_BAR"
private const val TAG_OFFSET = "TAG_OFFSET"
private const val KEY_OFFSET = -123

fun FragmentActivity.transparentScreen() {
    if (Build.VERSION.SDK_INT >= 21) {
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun FragmentActivity.fullscreen(isLightMode: Boolean = true) {
    statusBarLightMode(isLightMode)
    setStatusBarVisibility(false)
}

/**
 * 设置Activity 状态栏LightMode
 */
fun FragmentActivity.statusBarLightMode(isLightMode: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = window.decorView
        var visibility = decorView.systemUiVisibility
        visibility = if (isLightMode) {
            visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            visibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        decorView.systemUiVisibility = visibility
    }
}

fun FragmentActivity.setStatusBarVisibility(isVisible: Boolean = false) {
    if (isVisible) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        showStatusBarView(window)
        addMarginTopEqualStatusBarHeight(window)
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        hideStatusBarView(window)
        subtractMarginTopEqualStatusBarHeight(window)
    }
}

fun showStatusBarView(window: Window?) {
    val decorView = window?.decorView as ViewGroup?
    val fakeStatusBarView = decorView?.findViewWithTag<View>(TAG_STATUS_BAR) ?: return
    fakeStatusBarView.visibility = View.VISIBLE
}

fun hideStatusBarView(window: Window?) {
    val decorView = window?.decorView as ViewGroup?
    val fakeStatusBarView = decorView?.findViewWithTag<View>(TAG_STATUS_BAR) ?: return
    fakeStatusBarView.visibility = View.GONE
}

private fun addMarginTopEqualStatusBarHeight(window: Window?) {
    val tagView = window?.decorView?.findViewWithTag<View>(TAG_OFFSET) ?: return
    addMarginTopEqualStatusBarHeight(tagView)
}

private fun addMarginTopEqualStatusBarHeight(view: View) {
    view.tag = TAG_OFFSET
    val haveSetOffset = view.getTag(KEY_OFFSET)
    if (haveSetOffset != null && haveSetOffset as Boolean) return
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin + statusBarHeight,
            layoutParams.rightMargin,
            layoutParams.bottomMargin
    )
    view.setTag(KEY_OFFSET, true)
}

private fun subtractMarginTopEqualStatusBarHeight(window: Window?) {
    val tagView = window?.decorView?.findViewWithTag<View>(TAG_OFFSET) ?: return
    subtractMarginTopEqualStatusBarHeight(tagView)
}

private fun subtractMarginTopEqualStatusBarHeight(view: View) {
    view.tag = TAG_OFFSET
    val haveSetOffset = view.getTag(KEY_OFFSET)
    if (haveSetOffset == null || !(haveSetOffset as Boolean)) return
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin - statusBarHeight,
            layoutParams.rightMargin,
            layoutParams.bottomMargin
    )
    view.setTag(KEY_OFFSET, false)
}