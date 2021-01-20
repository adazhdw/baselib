package com.adazhdw.ktlib.ext

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.adazhdw.ktlib.core.lifecycle.LifecycleHandler

/**
 * FileName: ActivityExt
 * Author: adazhdw
 * Date: 2021/1/20 9:44
 * Description:关于Activity的扩展方法
 * History:
 */
inline fun <reified T : Activity> FragmentActivity.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(Intent(applicationContext, T::class.java).putExtrasEx(*extras))
}


fun FragmentActivity.toast(msg: CharSequence): Toast = toast(msg, Toast.LENGTH_SHORT)


fun FragmentActivity.getColorEx(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}


//post, postDelay
fun FragmentActivity.post(action: () -> Unit) {
    LifecycleHandler(this).post { action() }
}

fun FragmentActivity.postDelay(delay: Long = 0, action: () -> Unit) {
    LifecycleHandler(this).postDelayed({ action() }, delay)
}
