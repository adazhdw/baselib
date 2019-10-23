package com.adazhdw.ktlib.ext

import android.view.ViewStub

/**
 * author: daguozhu
 * created on: 2019/10/23 11:15
 * description:
 */

inline fun ViewStub.init() {
    if (parent != null) {
        inflate()
    }
}