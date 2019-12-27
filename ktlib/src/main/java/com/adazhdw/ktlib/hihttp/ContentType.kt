package com.adazhdw.ktlib.hihttp

import java.io.File


object ContentType {
    private val contentTypeMap = mapOf(
        ".doc" to "application/msword",
        ".docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        ".xls" to "application/vnd.ms-excel application/x-excel",
        ".xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        ".ppt" to "application/vnd.ms-powerpoint",
        ".pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        ".pps" to "application/vnd.ms-powerpoint",
        ".ppsx" to "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
        ".rtf" to "application/rtf",
        ".pdf" to "application/pdf",
        ".swf" to "application/x-shockwave-flash",
        ".dll" to "application/x-msdownload",
        ".tar" to "application/x-tar",
        ".tgz" to "application/x-compressed",
        ".zip" to "application/x-zip-compressed",
        ".z" to "application/x-compress",
        ".wav" to "audio/wav",
        ".wma" to "audio/x-ms-wma",
        ".wmv" to "video/x-ms-wmv",
        ".mp3" to "audio/mpeg",
        ".mp2" to "audio/mpeg",
        ".mpe" to "audio/mpeg",
        ".mpeg" to "audio/mpeg",
        ".mpg" to "audio/mpeg",
        ".rm" to "application/vnd.rn-realmedia",
        ".mid" to "audio/mid",
        ".midi" to "audio/mid",
        ".rmi" to "audio/mid",
        ".bmp" to "image/bmp",
        ".gif" to "image/gif",
        ".png" to "image/png",
        ".tif" to "image/tiff",
        ".tiff" to "image/tiff",
        ".jpe" to "image/jpeg",
        ".jpeg" to "image/jpeg",
        ".jpg" to "image/jpeg",
        ".txt" to "text/plain",
        ".xml" to "text/xml",
        ".html" to "text/html",
        ".css" to "text/css",
        ".js" to "text/javascript",
        ".mht" to "message/rfc822",
        ".mhtml" to "message/rfc822"
    )

    private const val otherType = "application/octet-stream"

    fun getType(file: File): String {
        val name = file.name
        val suffix = name.substring(name.indexOf("."), name.length)
        return contentTypeMap[suffix] ?: return otherType
    }
}