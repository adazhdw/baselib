package com.grantgz.baseapp.ext

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.adazhdw.baselibrary.LibUtil
import com.adazhdw.baselibrary.ext.installApk
import com.adazhdw.baselibrary.ext.logD
import com.adazhdw.baselibrary.utils.MimeUtil.getMimeType
import com.adazhdw.baselibrary.utils.PermissionUtil
import java.io.File


private val downloadMap = mutableMapOf<String?, Long>()
private val downloadManager: DownloadManager by lazy { LibUtil.getApp().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }
private val downloadReceiver by lazy { DownloadReceiver() }

/**
 * 下载文件
 */
fun FragmentActivity.downloadFile(
    url: String?,
    isPkgDown: Boolean = true,// 是否在应用默认包内目录下载
    isWifiDown: Boolean = true// 是否在wifi网络下下载
) {
    if (url.isNullOrBlank()) return
    val request = DownloadManager.Request(Uri.parse(url))
    //设置通知栏是否显示
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    //设置mimeType
    request.setMimeType(getMimeType(LibUtil.getApp(), url))
    //设置下载网络
    if (isWifiDown) {
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
    } else {
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
    }
    if (isPkgDown) {
        request.setDestinationInExternalFilesDir(LibUtil.getApp(), Environment.DIRECTORY_DOWNLOADS,
            getFileName(url)
        )
        download(request, url)
    } else {
        if (!PermissionUtil.isGranted(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            PermissionUtil.requestPermissions(
                permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                granted = {
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        getFileName(url)
                    )
                    download(request, url)
                })
        } else {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                getFileName(url)
            )
            download(request, url)
        }
    }
    registerReceiver(downloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestory() {
            unregisterReceiver(downloadReceiver)
        }
    })
}

private fun download(request: DownloadManager.Request, url: String) {
    val downloadId = downloadManager.enqueue(request)
    downloadMap[url] = downloadId
}

fun Context.cancelDown(url: String?) {
    val cancelId = downloadMap.remove(url) ?: return
    downloadManager.remove(cancelId)
}

fun Context.clearDown() {
    downloadMap.values.forEach {
        downloadManager.remove(it)
    }
    downloadMap.clear()
}

fun Context.getDownloadInfo(url: String?): DownloadInfo? {
    val infoId = downloadMap[url] ?: return null
    val cursor = downloadManager.query(DownloadManager.Query().setFilterById(infoId))
    if (cursor != null && cursor.moveToFirst()) {
        //下载文件的本地路径
        val localPath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
        //已经下载的字节数
        val hasDownByte = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
        //总共需要下载的字节数
        val allDownByte = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
        //Notification 标题
        val notificationTitle = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
        //Notification 描述
        val notificationDescription = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
        //下载对应的Id
        val downloadId = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
        //下载文件的url链接
        val downloadUrl = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))
        //下载文件的本地文件名称
        return DownloadInfo(
            localPath,
            hasDownByte,
            allDownByte,
            notificationTitle,
            notificationDescription,
            downloadId,
            downloadUrl
        )
    }
    return null
}

data class DownloadInfo(
    val localPath: String = "",
    val hasDownByte: Int = 0,
    val allDownByte: Int = 0,
    val title: String = "",
    val description: String = "",
    val downloadId: Long = 0,
    val downloadUrl: String = ""
)

fun getFileName(url: String?): String {
    if (url.isNullOrBlank()) return ""
    val namePos = url.lastIndexOf("/")
    return url.substring(namePos + 1)
}

private class DownloadReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "DownloadReceiver"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId != (-1L)) {
                val downList = downloadMap.filter { it.value == downloadId }.toList()
                if (downList.isNotEmpty()) {
                    val downloadInfo = context?.getDownloadInfo(downList[0].first)
                    val file = File(downloadInfo?.localPath?.substring(8)?:"")
                    logD(
                        TAG,
                        "------${downloadInfo.toString()}"
                    )
                    logD(
                        TAG,
                        "------${file.exists()}"
                    )
                    if (file.exists()) context?.installApk(File(downloadInfo?.localPath?:""))
                }
            }
        }
    }

}