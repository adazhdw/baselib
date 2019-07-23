package com.adazhdw.baselibrary.img

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.Uri.parse
import android.net.Uri.withAppendedPath
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.EXTRA_OUTPUT
import com.adazhdw.baselibrary.base.ForResultActivity
import com.adazhdw.baselibrary.utils.PermissionUtil


fun ForResultActivity.openAlbum() {
    albumPermission(granted = {
        val intent = Intent(ACTION_IMAGE_CAPTURE)
        intent.putExtra(EXTRA_OUTPUT, withAppendedPath(parse(cacheDir.path), "$.png"))
        startActivityForResultCompat(Intent(), resultCallback = { resultCode, data ->

        })
    }, denied = {

    })
}

private fun ForResultActivity.albumPermission(
    granted: ((Array<String>) -> Unit),
    denied: ((Array<String>) -> Unit)
) {
    PermissionUtil.requestPermissions(
        context = this,
        permissions = arrayOf(Manifest.permission_group.STORAGE),
        granted = {
            granted.invoke(it)
        },
        denied = {
            denied.invoke(it)
        })
}


/**
 * 扫描指定的文件
 * 用途：从本软件新增、修改、删除图片、文件某一个文件（音频、视频）需要更新系统媒体库时使用，不必扫描整个SD卡
 * @param uri
 */
fun updateAlum(context: Context, uri: Uri) {
    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
}