package com.adazhdw.baselibrary.img

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import androidx.core.content.FileProvider
import com.adazhdw.baselibrary.base.ForResultActivity
import com.adazhdw.baselibrary.ext.logD
import com.adazhdw.baselibrary.utils.PermissionUtil
import com.adazhdw.baselibrary.utils.UriUtil
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun ForResultActivity.selectImage(
    onResult: ((imgUri: Uri?, file: File?) -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    PermissionUtil.requestPermissions(
        context = this,
        permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        granted = {
            Intent(Intent.ACTION_GET_CONTENT).setType("image/*").also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResultCompat(intent, resultCallback = { resultCode, data ->
                        when (resultCode) {
                            RESULT_OK -> {
                                val imgUri: Uri? = data?.data
                                onResult?.invoke(imgUri, UriUtil.getFileByUri(this, imgUri))
                            }
                            RESULT_CANCELED -> onCancel?.invoke()
                            else -> onError?.invoke("No image selected")
                        }
                    })
                }
            }
            logD("ACTION_GET_CONTENT-image/*")
        },
        denied = {
            onError?.invoke("Permission Denied")
        })
}

fun ForResultActivity.captureImage(
    onResult: ((imgUri: Uri?, file: File) -> Unit)? = null,
    onError: ((String?) -> Unit)? = null
) {
    PermissionUtil.requestPermissions(
        context = this,
        permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ),
        granted = {
            Intent(ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    try {
                        createImageFile()
                    } catch (io: IOException) {
                        onError?.invoke(io.message)
                        null
                    }?.also { file ->
                        val photoURI: Uri? = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResultCompat(intent, resultCallback = { resultCode, data ->
                            if (resultCode == RESULT_OK) {
                                onResult?.invoke(photoURI, file)
                            } else {
                                onError?.invoke("No image selected")
                            }
                        })
                    }
                }
            }
            logD("ACTION_IMAGE_CAPTURE")
        },
        denied = {
            onError?.invoke("Permission Denied")
        })
}

@Throws(IOException::class)
private fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date(System.currentTimeMillis()))
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
    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
}