package com.adazhdw.ktlib.kthttp.callback

interface ProgressCallback {
    fun updateProgress(progress: Int, networkSpeed: Long, done: Boolean)
}