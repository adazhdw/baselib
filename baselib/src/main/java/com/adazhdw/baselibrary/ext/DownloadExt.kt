package com.adazhdw.baselibrary.ext

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.adazhdw.baselibrary.LibUtil


object DownloadExt {
    private val downloadMap = mutableMapOf<String?, Long>()
    private val downloadManager: DownloadManager by lazy { LibUtil.getApp().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

    fun download(url: String?) {
        val downloadRequest = DownloadManager.Request(Uri.parse(url))
        val downloadId = downloadManager.enqueue(downloadRequest)
        downloadMap[url] = downloadId
    }

    fun downloadApk(url: String?) {
        val downloadRequest = DownloadManager.Request(Uri.parse(url))
        downloadRequest.setMimeType("application/vnd.android.package-archive")
        val downloadId = downloadManager.enqueue(downloadRequest)
        downloadMap[url] = downloadId
    }

    fun cancel(url: String?) {
        val cancelId = downloadMap.remove(url) ?: return
        downloadManager.remove(cancelId)
    }

    fun clear() {
        downloadMap.values.forEach {
            downloadManager.remove(it)
        }
        downloadMap.clear()
    }

}