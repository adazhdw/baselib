@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


inline fun <reified T : Activity> Fragment.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(Intent(context, T::class.java).putExtrasEx(*extras))
}

inline fun <reified T : Activity> FragmentActivity.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(Intent(applicationContext, T::class.java).putExtrasEx(*extras))
}


fun Fragment.toast(msg: CharSequence): Toast? = context?.toast(msg, Toast.LENGTH_SHORT)

fun FragmentActivity.toast(msg: CharSequence): Toast = toast(msg, Toast.LENGTH_SHORT)

fun Context.toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast.makeText(this, msg, duration).apply { show() }


fun Fragment.getColorEx(@ColorRes res: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res, context?.theme)
    } else {
        resources.getColor(res)
    }
}

fun FragmentActivity.getColorEx(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}

fun Context.getColorEx(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}
