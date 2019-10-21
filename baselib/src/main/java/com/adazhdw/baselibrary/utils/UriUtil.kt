package com.adazhdw.baselibrary.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import com.adazhdw.baselibrary.ext.loge


import com.blankj.utilcode.util.Utils
import java.io.File

object UriUtil {

    fun getFileByUri(context: Context, uri: Uri?): File? {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            when {
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val type = split[0]
                    if ("primary".equals(type, true)) {
                        return File(Environment.getExternalStorageDirectory().absolutePath + File.separator + split[1])
                    }
                }
                isDownloadsDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(uri, java.lang.Long.valueOf(docId))
                    return getFileFromUri(contentUri, 2)
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":")
                    val contentUri = when (split[0]) {
                        "image" -> {
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                        else -> {
                            loge(content = "$uri parse failed. -> 3")
                            return null
                        }
                    }
                    val selection = "_id=?"
                    val selectionArgs: Array<String> = arrayOf(split[1])
                    return getFileFromUri(contentUri, selection, selectionArgs, 4)
                }
                ContentResolver.SCHEME_CONTENT == uri?.scheme -> {
                    return getFileFromUri(uri, 5)
                }
                else -> {
                    loge(content = "$uri parse failed. -> 6")
                    return null
                }
            }
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri?): Boolean {
        return "com.android.externalstorage.documents" == uri?.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri?): Boolean {
        return "com.android.providers.downloads.documents" == uri?.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri?): Boolean {
        return "com.android.providers.media.documents" == uri?.authority
    }


    private fun getFileFromUri(uri: Uri, code: Int): File? {
        return getFileFromUri(uri, null, null, code)
    }

    private fun getFileFromUri(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?,
        code: Int
    ): File? {
        val cursor = Utils.getApp().contentResolver.query(
            uri, arrayOf("_data"), selection, selectionArgs, null
        )
        if (cursor == null) {
            loge(content = "$uri parse failed(cursor is null). -> $code")
            return null
        }
        try {
            return if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex("_data")
                if (columnIndex > -1) {
                    File(cursor.getString(columnIndex))
                } else {
                    loge(content = "$uri parse failed(columnIndex: $columnIndex is wrong). -> $code")
                    null
                }
            } else {
                loge(content = "$uri parse failed(moveToFirst return false). -> $code")
                null
            }
        } catch (e: Exception) {
            loge(content = "$uri parse failed. -> $code")
            return null
        } finally {
            cursor.close()
        }
    }
}