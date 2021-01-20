@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * author: daguozhu
 * created on: 2019/10/21 17:36
 * description:
 */


inline fun <reified T : Activity> Fragment.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(Intent(context, T::class.java).putExtrasEx(*extras))
}


fun Fragment.toast(msg: CharSequence): Toast? = context?.toast(msg, Toast.LENGTH_SHORT)

fun Fragment.getColorEx(@ColorRes res: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res, context?.theme)
    } else {
        resources.getColor(res)
    }
}


fun FragmentActivity.addFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    supportFragmentManager.transact(isAllowingStateLose) {
        add(frameId, fragment)
    }
}

fun Fragment.addFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    childFragmentManager.transact(isAllowingStateLose) {
        add(frameId, fragment)
    }
}

fun FragmentActivity.showFragment(fragment: Fragment, isAllowingStateLose: Boolean = false) {
    supportFragmentManager.transact(isAllowingStateLose) {
        show(fragment)
    }
}

fun Fragment.showFragment(fragment: Fragment, isAllowingStateLose: Boolean = false) {
    childFragmentManager.transact(isAllowingStateLose) {
        show(fragment)
    }
}

fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    supportFragmentManager.transact(isAllowingStateLose) {
        replace(frameId, fragment)
    }
}

fun Fragment.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    childFragmentManager.transact(isAllowingStateLose) {
        replace(frameId, fragment)
    }
}

fun FragmentActivity.hideFragment(
    fragment: Fragment,
    isAllowingStateLose: Boolean = false
) {
    supportFragmentManager.transact(isAllowingStateLose) {
        hide(fragment)
    }
}

fun Fragment.hideFragment(
    fragment: Fragment,
    isAllowingStateLose: Boolean = false
) {
    childFragmentManager.transact(isAllowingStateLose) {
        hide(fragment)
    }
}

fun FragmentManager.transact(
    isAllowingStateLose: Boolean = false,
    action: (FragmentTransaction.() -> Unit)
) {
    val transaction = beginTransaction()
    transaction.action()
    if (isAllowingStateLose) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

