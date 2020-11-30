package com.adazhdw.ktlib.ext

import android.app.ActionBar
import android.os.Build
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.adazhdw.ktlib.utils.StatusBarUtil

fun FragmentActivity.transparent(){
    StatusBarUtil.setTranslucent(this)
}

fun FragmentActivity.fullscreen(isLightMode: Boolean = true) {
    statusBarLightMode(isLightMode)
    StatusBarUtil.setStatusBarVisibility(this,false)
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

//重写 onSupportNavigateUp 方法，返回 true
fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    actionBar?.run {
        action()
    }
}

fun AppCompatActivity.setActionbarCompact(@IdRes toolbarId: Int) {
    setupActionBar(toolbarId) {
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowHomeEnabled(true)
    }
}
