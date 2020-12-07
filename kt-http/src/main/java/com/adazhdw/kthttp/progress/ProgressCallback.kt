package com.adazhdw.kthttp.progress


/**
 * Author: dgz
 * Date: 2020/8/21 14:50
 * Description:
 */
interface ProgressCallback {
    fun updateProgress(progress: Int, networkSpeed: Long, done: Boolean)
    fun onProgress(progress: Int, currentSize: Long, totalSize: Long)
}