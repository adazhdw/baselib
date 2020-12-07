package com.adazhdw.kthttp.ext

import android.util.Log
import com.adazhdw.kthttp.KtConfig


const val TAG = "LogExt"

private enum class LEVEL {
    V, D, I, W, E
}

fun String?.logV(tag: String? = TAG) = log(tag, this, LEVEL.V)
fun String?.logD(tag: String? = TAG) = log(tag, this, LEVEL.D)
fun String?.logI(tag: String? = TAG) = log(tag, this, LEVEL.I)
fun String?.logW(tag: String? = TAG) = log(tag, this, LEVEL.W)
fun String?.logE(tag: String? = TAG) = log(tag, this, LEVEL.E)

private fun log(tag: String?, content: String?, level: LEVEL) {
    if (!KtConfig.isDebug) return
    when (level) {
        LEVEL.V -> {
            Log.v(tag, content ?: "")
        }
        LEVEL.D -> {
            Log.d(tag, content ?: "")
        }
        LEVEL.I -> {
            Log.i(tag, content ?: "")
        }
        LEVEL.W -> {
            Log.w(tag, content ?: "")
        }
        LEVEL.E -> {
            Log.e(tag, content ?: "")
        }
    }
}
