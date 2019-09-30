@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.baselibrary.ext

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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

fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int,isAllowingStateLose:Boolean = false) {
    supportFragmentManager.transact(isAllowingStateLose) {
        replace(frameId, fragment)
    }
}

fun FragmentActivity.addFragment(fragment: Fragment, frameId: Int,isAllowingStateLose:Boolean = false) {
    supportFragmentManager.transact(isAllowingStateLose) {
        add(frameId, fragment)
    }
}

fun Fragment.replaceFragment(fragment: Fragment, frameId: Int,isAllowingStateLose:Boolean = false) {
    childFragmentManager.transact(isAllowingStateLose) {
        replace(frameId, fragment)
    }
}

fun Fragment.addFragment(fragment: Fragment, frameId: Int,isAllowingStateLose:Boolean = false) {
    childFragmentManager.transact(isAllowingStateLose) {
        add(frameId, fragment)
    }
}

fun FragmentManager.transact(isAllowingStateLose:Boolean = false,action: (FragmentTransaction.() -> Unit)) {
    val transaction = beginTransaction()
    transaction.action()
    if (isAllowingStateLose){
        transaction.commitAllowingStateLoss()
    }else{
        transaction.commit()
    }
}

