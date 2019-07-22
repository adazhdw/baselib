package com.adazhdw.baselibrary.img

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri.parse
import android.net.Uri.withAppendedPath
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.MediaStore.EXTRA_OUTPUT
import com.adazhdw.baselibrary.base.ForResultActivity
import com.adazhdw.baselibrary.ext.PermissionExt
import com.adazhdw.baselibrary.ext.getImgFormat


fun ForResultActivity.openAlbum() {
    if (!PermissionExt.isGranted(arrayOf(Manifest.permission_group.STORAGE))) {
        PermissionExt.requestPermissions(
            context = this,
            permissions = arrayOf(Manifest.permission_group.STORAGE),
            granted = {
                val intent = Intent(ACTION_IMAGE_CAPTURE)
                intent.putExtra(EXTRA_OUTPUT, withAppendedPath(parse(cacheDir.path), "${getImgFormat()}.png"))
                startActivityForResultCompat(Intent(), resultCallback = { resultCode, data ->
                    if (resultCode == RESULT_OK) {

                    }
                })
            },
            denied = {

            })
    } else {
        startActivityForResultCompat(Intent(), resultCallback = { resultCode, data ->
            if (resultCode == RESULT_OK) {

            }
        })
    }
}