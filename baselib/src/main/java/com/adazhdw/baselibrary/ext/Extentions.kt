@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.baselibrary.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.adazhdw.baselibrary.LibUtil
import com.adazhdw.baselibrary.mvvm.KotlinViewModelProvider


inline fun <reified T : Activity> Fragment.startActivity() {
    startActivity(Intent(context, T::class.java))
}

inline fun <reified T : Activity> FragmentActivity.startActivity() {
    startActivity(Intent(applicationContext, T::class.java))
}

inline fun Fragment.toast(msg: CharSequence): Toast =
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).apply { show() }

inline fun FragmentActivity.toast(msg: CharSequence): Toast =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).apply { show() }

inline fun Context.toast(msg: CharSequence): Toast =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).apply { show() }

inline fun toast(msg: CharSequence): Toast =
    Toast.makeText(LibUtil.getApp(), msg, Toast.LENGTH_SHORT).apply { show() }

inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {
    return KotlinViewModelProvider.of(this, factory)
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {
    return KotlinViewModelProvider.of(this, factory)
}
