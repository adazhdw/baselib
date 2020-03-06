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
import com.adazhdw.ktlib.LibUtil


inline fun <reified T : Activity> Fragment.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(Intent(context, T::class.java).putExtrasEx(*extras))
}

inline fun <reified T : Activity> FragmentActivity.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(Intent(applicationContext, T::class.java).putExtrasEx(*extras))
}


inline fun Fragment.toast(msg: CharSequence): Toast =
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).apply { show() }

inline fun FragmentActivity.toast(msg: CharSequence): Toast =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).apply { show() }

inline fun Context.toast(msg: CharSequence): Toast =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).apply { show() }

inline fun toast(msg: CharSequence): Toast =
    Toast.makeText(LibUtil.getApp(), msg, Toast.LENGTH_SHORT).apply { show() }


inline fun Fragment.getColorEx(@ColorRes res:Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res,context?.theme)
    }else{
        resources.getColor(res)
    }
}

inline fun FragmentActivity.getColorEx(@ColorRes res:Int): Int {
    return ContextCompat.getColor(this,res)
}

inline fun Context.getColorEx(@ColorRes res:Int): Int {
    return ContextCompat.getColor(this,res)
}
