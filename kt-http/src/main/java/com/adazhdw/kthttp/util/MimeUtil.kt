package com.adazhdw.kthttp.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.util.*


object MimeUtil {
    fun getMimeType(context: Context, url: String?): String? {
        val uri = Uri.parse(url)
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cr = context.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(fileExtension.toLowerCase(Locale.CHINA))
        }
    }

    fun getMimeType(url: String?): String {
        val uri = Uri.parse(url)
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        return MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(fileExtension.toLowerCase(Locale.CHINA)) ?: ""
    }

    fun getMediaType(url: String?): MediaType? {
        val uri = Uri.parse(url)
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        return (MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(fileExtension.toLowerCase(Locale.CHINA))
            ?: "").toMediaTypeOrNull()
    }
}