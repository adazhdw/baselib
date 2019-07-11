package com.adazhdw.baselibrary.ext

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.adazhdw.baselibrary.utils.PermissionUtils

import java.io.File
import java.util.Arrays


private val TAG = "FileUtils"
/**
 * 文件夹排序
 * @param files
 */
fun sortFiles(files: Array<File>) {
    Arrays.sort(files) { lhs, rhs ->
        //返回负数表示o1 小于o2，返回0 表示o1和o2相等，返回正数表示o1大于o2。
        val l1 = lhs.isDirectory
        val l2 = rhs.isDirectory
        if (l1 && !l2)
            -1
        else if (!l1 && l2)
            1
        else {
            lhs.name.compareTo(rhs.name)
        }
    }
}

/**
 * 扫描指定的文件
 * 用途：从本软件新增、修改、删除图片、文件某一个文件（音频、视频）需要更新系统媒体库时使用，不必扫描整个SD卡
 * @param uri
 */
fun updateAlum(context: Context, uri: Uri) {
    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
}

/**
 * create folder
 */
fun Context.createFolder(path: String): Boolean {
    if (PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
        val file = File(path)
        return if (!file.exists()) {
            file.mkdirs()
        } else {
            true
        }
    } else {
        var success = false
        PermissionUtils.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            onGranted = { onGranted ->
                if (onGranted.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    val file = File(path)
                    if (!file.exists()) {
                        success = file.mkdirs()
                    }
                }
            })
        return success
    }
}
