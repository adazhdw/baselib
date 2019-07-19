package com.adazhdw.baselibrary.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


fun <T : Fragment> FragmentManager.showFragment(clazz: Class<T>) {
    beginTransaction()
        .add(clazz.newInstance(), clazz.simpleName)
        .commitAllowingStateLoss()
}