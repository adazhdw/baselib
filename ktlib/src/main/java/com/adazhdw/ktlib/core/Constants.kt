package com.adazhdw.ktlib.core

import android.os.Looper

/**
 * author: daguozhu
 * created on: 2019/10/22 18:54
 * description:
 */

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()