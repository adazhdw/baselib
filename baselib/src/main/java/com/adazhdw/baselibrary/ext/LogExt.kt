package com.adazhdw.baselibrary.ext

import android.util.Log
import com.adazhdw.baselibrary.isDebug


fun Any.logE(content: String?) {
    if (isDebug) {
        logE(this.javaClass.simpleName, content)
    }
}

fun Any.logE(content: Throwable?) {
    if (isDebug) {
        logE(this.javaClass.simpleName, content?.message)
    }
}

fun logE(tag: String?, content: String?) {
    if (isDebug) {
        Log.e(tag, content)
    }
}

fun Any.logD(content: String?) {
    if (isDebug) {
        logD(this.javaClass.simpleName, content)
    }
}

fun logD(tag: String?, content: String?) {
    if (isDebug) {
        Log.d(tag, content)
    }
}
