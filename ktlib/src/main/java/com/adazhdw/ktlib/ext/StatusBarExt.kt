package com.adazhdw.ktlib.ext

import android.os.Build
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.adazhdw.ktlib.utils.StatusBarUtils

fun FragmentActivity.transparent(){
    StatusBarUtils.setTranslucent(this)
}

fun FragmentActivity.fullscreen(isLightMode: Boolean = true) {
    statusBarLightMode(isLightMode)
    StatusBarUtils.setStatusBarVisibility(this,false)
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
