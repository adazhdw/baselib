package com.adazhdw.baselibrary.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * author: daguozhu
 * created on: 2019/10/21 17:36
 * description:
 */


fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    supportFragmentManager.transact(isAllowingStateLose) {
        replace(frameId, fragment)
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

fun Fragment.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    isAllowingStateLose: Boolean = false
) {
    childFragmentManager.transact(isAllowingStateLose) {
        replace(frameId, fragment)
    }
}

fun Fragment.addFragment(fragment: Fragment, frameId: Int, isAllowingStateLose: Boolean = false) {
    childFragmentManager.transact(isAllowingStateLose) {
        add(frameId, fragment)
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

