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
import com.adazhdw.ktlib.base.activity.ForResultActivity
import com.adazhdw.ktlib.ext.logD
import com.adazhdw.ktlib.utils.UriUtil
import com.adazhdw.ktlib.utils.permission.KtPermission
import com.adazhdw.ktlib.utils.permission.PermissionCallback
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

data class DocumentModel(val uri: Uri, val file: File)

fun ForResultActivity.selectImage(
    onResult: ((documentModel: DocumentModel) -> Unit),
    onDenied: ((denied: List<String>) -> Unit),
    onError: ((String) -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    KtPermission.request(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE,
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
                                    UriUtil.getFileByUri(this@selectImage, data)?.also { file ->
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


fun ForResultActivity.captureImage(
    onResult: ((model: DocumentModel) -> Unit),
    onDenied: ((denied: List<String>) -> Unit),
    onError: ((String) -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    KtPermission.request(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        callback = object : PermissionCallback {
            override fun invoke(p1: Boolean, p2: List<String>) {
                if (p1) {
                    Intent(ACTION_IMAGE_CAPTURE).also { intent ->
                        intent.resolveActivity(packageManager)?.also {
                            try {
                                createImageFile()
                            } catch (io: IOException) {
                                onError?.invoke(io.message ?: "")
                                null
                            }?.also { file ->
                                val photoURI: Uri = getUriForFile(file)
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                launch {
                                    //协程方法启动intent
                                    startActivityForResultCoroutines(
                                        intent,
                                        onFailure = { onError?.invoke("Image data is null") },
                                        onCancel = { onCancel?.invoke() })
                                    updateAlum(this@captureImage, photoURI)
                                    onResult.invoke(DocumentModel(photoURI, file))
                                }
                            }
                        }
                    }
                    ("ACTION_IMAGE_CAPTURE").logD()
                } else {
                    onDenied.invoke(p2)
                }
            }
        })
}

fun Context.getUriForFile(file: File): Uri {
    return FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
}

@Throws(IOException::class)
private fun Context.createImageFile(): File {
    val timeStamp =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date(System.currentTimeMillis()))
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}


/**
 * 扫描指定的文件
 * 用途：从本软件新增、修改、删除图片、文件某一个文件（音频、视频）需要更新系统媒体库时使用，不必扫描整个SD卡
 * @param uri
 */
fun updateAlum(context: Context, uri: Uri) {
    context.sendBroadcast(Intent(ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
}