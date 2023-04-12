package com.adazhdw.ktlib.ext

import android.content.Intent

fun Intent.putExtrasEx(vararg extras: Pair<String, Any?>): Intent {
    if (extras.isEmpty()) return this
    putExtras(bundleOf(*extras))
    return this
}