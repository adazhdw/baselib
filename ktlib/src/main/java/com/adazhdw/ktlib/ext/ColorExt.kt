@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * author: daguozhu
 * created on: 2019/11/5 15:00
 * description:
 */



inline fun Fragment.getColorEx(@ColorRes res:Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res,null)
    }else{
        resources.getColor(res)
    }
}

inline fun FragmentActivity.getColorEx(@ColorRes res:Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res,null)
    }else{
        resources.getColor(res)
    }
}

inline fun Context.getColorEx(@ColorRes res:Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res,null)
    }else{
        resources.getColor(res)
    }
}
