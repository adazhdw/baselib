@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * author: daguozhu
 * created on: 2019/10/21 17:36
 * description:
 */


inline fun FragmentActivity.addFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    supportFragmentManager.transact(isAllowingStateLose) {
        add(frameId, fragment)
    }
}

inline fun Fragment.addFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    childFragmentManager.transact(isAllowingStateLose) {
        add(frameId, fragment)
    }
}

inline fun FragmentActivity.showFragment(fragment: Fragment, isAllowingStateLose: Boolean = false) {
    supportFragmentManager.transact(isAllowingStateLose) {
        show(fragment)
    }
}

inline fun Fragment.showFragment(fragment: Fragment, isAllowingStateLose: Boolean = false) {
    childFragmentManager.transact(isAllowingStateLose) {
        show(fragment)
    }
}

inline fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    supportFragmentManager.transact(isAllowingStateLose) {
        replace(frameId, fragment)
    }
}

inline fun Fragment.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    childFragmentManager.transact(isAllowingStateLose) {
        replace(frameId, fragment)
    }
}

inline fun FragmentActivity.hideFragment(
    fragment: Fragment,
    isAllowingStateLose: Boolean = false
) {
    supportFragmentManager.transact(isAllowingStateLose) {
        hide(fragment)
    }
}

inline fun Fragment.hideFragment(
    fragment: Fragment,
    isAllowingStateLose: Boolean = false
) {
    childFragmentManager.transact(isAllowingStateLose) {
        hide(fragment)
    }
}

inline fun FragmentManager.transact(
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

