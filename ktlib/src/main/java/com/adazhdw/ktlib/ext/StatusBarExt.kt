package com.adazhdw.ktlib.ext

import androidx.fragment.app.FragmentActivity
import com.adazhdw.ktlib.utils.StatusBarUtil

fun FragmentActivity.transparent() {
    StatusBarUtil.setTranslucent(this)
}

fun FragmentActivity.fullscreen(isLightMode: Boolean = true) {
    statusBarLightMode(isLightMode)
    StatusBarUtil.setStatusBarVisibility(this, false)
}

/**
 * 设置Activity 状态栏LightMode
 */
fun FragmentActivity.statusBarLightMode(isLightMode: Boolean = true) {
    StatusBarUtil.setStatusBarLightMode(this, isLightMode)
}