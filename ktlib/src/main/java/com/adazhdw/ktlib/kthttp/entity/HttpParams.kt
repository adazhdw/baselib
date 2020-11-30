package com.adazhdw.ktlib.kthttp.entity

import com.adazhdw.ktlib.kthttp.KtHttp
import com.adazhdw.ktlib.kthttp.constant.HttpConstant
import com.adazhdw.ktlib.utils.MimeUtil
import okhttp3.MediaType
import java.io.File

/**
 * author：daguozhu
 * date-time：2020/11/17 16:40
 * description：请求参数封装
 * @param isMultipart /** 是否有流参数 */
 **/
class HttpParams(val isMultipart: Boolean) {
    /** 请求头存放集合 */
    internal val mParams: HashMap<String, String> = KtHttp.ktHttp.getCommonParams()

    /** 上传文件集合 */
    internal val files: MutableList<Part> = mutableListOf()

    fun put(key: String, value: String) {
        mParams[key] = value
    }

    fun putAll(params: Map<String, String>) {
        mParams.putAll(params)
    }

    fun remove(key: String) {
        mParams.remove(key)
    }

    fun get(key: String): String? = mParams[key]

    fun isEmpty(): Boolean = mParams.isEmpty()

    fun addFormDataPart(key: String, file: File) {
        if (!file.exists() || file.length() == 0L) return
        val isPng = file.name.indexOf("png") > 0 || file.name.indexOf("PNG") > 0
        if (isPng) {
            this.files.add(Part(key, Part.FileWrapper(file, HttpConstant.PNG)))
            return
        }
        val isJpg = file.name.indexOf("jpg") > 0 || file.name.indexOf("JPG") > 0
                || file.name.indexOf("jpeg") > 0 || file.name.indexOf("JPEG") > 0
        if (isJpg) {
            this.files.add(Part(key, Part.FileWrapper(file, HttpConstant.JPG)))
            return
        }
        if (!isPng && !isJpg) {
            this.files.add(Part(key, Part.FileWrapper(file, mediaType(file))))
        }
    }

    fun addFormDataPart(map: Map<String, File>) {
        for ((key, file) in map) addFormDataPart(key, file)
    }

    private fun mediaType(file: File): MediaType? = MimeUtil.getMediaType(file.path)

}