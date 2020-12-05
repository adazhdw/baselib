package com.adazhdw.ktlib.ext

import android.app.ActionBar
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

/**
 * author：daguozhu
 * date-time：2020/12/3 16:09
 * description：
 **/


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


