package com.adazhdw.baselibrary.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap


object MimeUtil{
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
}