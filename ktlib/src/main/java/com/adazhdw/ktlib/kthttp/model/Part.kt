package com.adazhdw.ktlib.kthttp.model

import okhttp3.MediaType
import java.io.File

/**
 * author：daguozhu
 * date-time：2020/9/1 20:18
 * description：
 **/
data class Part(
    val key: String,
    val wrapper: FileWrapper
) {
    class FileWrapper(
        val file: File,
        val mediaType: MediaType?
    ) {
        val fileName: String
            get() = file.name
    }
}