package com.adazhdw.ktlib.img

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import androidx.core.content.FileProvider
import com.adazhdw.ktlib.base.ForResultActivity
import com.adazhdw.ktlib.ext.logD

import com.adazhdw.ktlib.utils.PermissionUtil
import com.adazhdw.ktlib.utils.UriUtil
import com.adazhdw.ktlib.utils.permission.KtPermission
import com.adazhdw.ktlib.utils.permission.PermissionCallback
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun ForResultActivity.selectImage2(
    onResult: ((documentModel: DocumentModel) -> Unit),
    onDenied: ((denied: List<String>) -> Unit),
    onError: ((String) -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    KtPermission.request(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        callback = object : PermissionCallback {
            override fun invoke(p1: Boolean, p2: List<String>) {
                if (p1) {
                    Intent(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                            ACTION_OPEN_DOCUMENT else ACTION_GET_CONTENT
                    ).setType("image/*").addCategory(CATEGORY_OPENABLE).also { intent ->
                        intent.resolveActivity(packageManager)?.also {
                            launch {
                                //协程方法启动intent
                                startActivityForResultCoroutines(
                                    intent,
                                    onFailure = { onError?.invoke("Image data is null") },
                                    onCancel = { onCancel?.invoke() })?.data?.also { data ->
                                    UriUtil.getFileByUri(this@selectImage2, data)?.also { file ->
                                        onResult.invoke(DocumentModel(data, file))
                                    }
                                }
                            }
                        }
                    }
                    ("ACTION_GET_CONTENT-image/*").logD()
                } else {
                    onDenied.invoke(p2)
                }
            }
        })
}