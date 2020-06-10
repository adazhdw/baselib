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
import com.adazhdw.ktlib.KtLib


inline fun <reified T : Activity> Fragment.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(Intent(context, T::class.java).putExtrasEx(*extras))
}

inline fun <reified T : Activity> FragmentActivity.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(Intent(applicationContext, T::class.java).putExtrasEx(*extras))
}


fun Fragment.toast(msg: CharSequence): Toast =
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).apply { show() }

fun FragmentActivity.toast(msg: CharSequence): Toast =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).apply { show() }

fun Context.toast(msg: CharSequence): Toast =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).apply { show() }

fun toast(msg: CharSequence): Toast =
    Toast.makeText(KtLib.getApp(), msg, Toast.LENGTH_SHORT).apply { show() }


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
