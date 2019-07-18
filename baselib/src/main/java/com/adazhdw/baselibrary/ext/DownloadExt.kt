package com.adazhdw.baselibrary.ext

import android.Manifest
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import com.adazhdw.baselibrary.LibUtil
import java.io.File


private val URL = "https://static.usasishu.com/com.uuabc.samakenglish_5.1.12_35.apk"
private val downloadMap = mutableMapOf<String?, Long>()
val downloadManager: DownloadManager by lazy { LibUtil.getApp().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

/**
 * 下载文件
 */
fun DownloadManager.downloadFile(url: String?, isPackageDown: Boolean = true) {
    if (url.isNullOrBlank()) return
    val request = DownloadManager.Request(Uri.parse(url))
    val dirType = Environment.DIRECTORY_DOWNLOADS
    if (isPackageDown) {
        request.setDestinationInExternalFilesDir(LibUtil.getApp(), dirType, getFileName(url))
        download(request, url)
    } else {
        if (!PermissionExt.isGranted(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            PermissionExt.requestPermissions(
                permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                granted = {
                    request.setDestinationInExternalPublicDir(dirType, getFileName(url))
                    download(request, url)
                })
        } else {
            request.setDestinationInExternalPublicDir(dirType, getFileName(url))
            download(request, url)
        }
    }
}

/**
 * 下载apk
 */
fun DownloadManager.downloadApk(url: String?) {
    if (url.isNullOrBlank() || !url.endsWith(".apk")) return
    val request = DownloadManager.Request(Uri.parse(url))
    request.setMimeType("application/vnd.android.package-archive")
    download(request, url)
}

private fun download(request: DownloadManager.Request, url: String) {
    val downloadId = downloadManager.enqueue(request)
    downloadMap[url] = downloadId
}

fun DownloadManager.cancel(url: String?) {
    val cancelId = downloadMap.remove(url) ?: return
    downloadManager.remove(cancelId)
}

fun DownloadManager.clear() {
    downloadMap.values.forEach {
        downloadManager.remove(it)
    }
    downloadMap.clear()
}

fun DownloadManager.getDownloadInfo(url: String?): DownloadInfo? {
    val infoId = downloadMap[url] ?: return null
    val query = DownloadManager.Query()
    val cursor = downloadManager.query(query.setFilterById(infoId))
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
        //下载文件的本地文件名称
        val loadlFileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME))
        //下载文件的url链接
        val downloadUrl = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))
        return DownloadInfo(
            localPath,
            hasDownByte,
            allDownByte,
            notificationTitle,
            notificationDescription,
            downloadId,
            loadlFileName,
            downloadUrl
        )
    }
    return null
}

private fun initDownloadPath(isPackageDown: Boolean): String {
    return if (isPackageDown) {
        val file = File("${LibUtil.getApp().packageName}/")
        if (!file.exists()) file.mkdirs()
        file.absolutePath
    } else Environment.DIRECTORY_DOWNLOADS
}

fun getFileName(url: String?): String {
    if (url.isNullOrBlank()) return ""
    val namePos = url.lastIndexOf("/")
    return url.substring(namePos + 1)
}

data class DownloadInfo(
    val localPath: String = "",
    val hasDownByte: Int = 0,
    val allDownByte: Int = 0,
    val title: String = "",
    val description: String = "",
    val downloadId: Long = 0,
    val fileName: String = "",
    val downloadUrl: String = ""
)

fun getMimeType(context: Context, uri: Uri): String? {
    return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        val cr = context.contentResolver
        cr.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
    }
}

fun getMimeType(context: Context, url: String?): String? {
    val uri = Uri.parse(url)
    return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        val cr = context.contentResolver
        cr.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
    }
}