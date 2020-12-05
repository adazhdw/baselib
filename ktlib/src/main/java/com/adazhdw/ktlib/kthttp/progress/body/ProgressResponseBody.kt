package com.adazhdw.ktlib.kthttp.progress.body

import com.adazhdw.ktlib.kthttp.progress.ProgressCallback
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * author：daguozhu
 * date-time：2020/9/2 10:22
 * description：
 **/
class ProgressResponseBody(
    response: Response,
    private val progressCallback: ProgressCallback
) : ResponseBody() {

    companion object {
        private const val MIN_INTERVAL = 50
    }

    private val responseBody: ResponseBody? = response.body
    private var contentLength: Long = 0L //ResponseBody 内容长度，部分接口拿不到，会返回-1，此时会没有进度回调
    private var bufferedSource: BufferedSource? = null    //包装完成的BufferedSource

    init {
        if (responseBody != null) contentLength = responseBody.contentLength()
        if (contentLength == -1L) contentLength = getContentLengthByHeader(response)
    }

    override fun contentLength(): Long = contentLength

    override fun contentType(): MediaType? = responseBody?.contentType()

    override fun source(): BufferedSource {
        if (bufferedSource == null && responseBody != null)
            bufferedSource = source(responseBody.source()).buffer()
        return bufferedSource!!
    }

    /**
     * 读取，回调进度接口
     *
     * @param source Source
     * @return Source
     */
    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            //当前读取字节数
            var totalBytesRead = 0L
            var lastProgress //上次回调进度
                    = 0
            var lastTime //上次回调时间
                    : Long = 0

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                if (bytesRead == -1L) {   //-1 代表读取完毕
                    if (contentLength == -1L) contentLength = totalBytesRead
                } else {
                    totalBytesRead += bytesRead //未读取完，则累加已读取的字节
                }

                //当前进度 = 当前已读取的字节 / 总字节
                val currentProgress = (totalBytesRead * 100 / contentLength).toInt()
                if (currentProgress > lastProgress) {  //前进度大于上次进度，则更新进度
                    if (currentProgress < 100) {
                        val currentTime = System.currentTimeMillis()
                        //两次回调时间小于 MIN_INTERVAL 毫秒，直接返回，避免更新太频繁
                        if (currentTime - lastTime < MIN_INTERVAL) return bytesRead
                        lastTime = currentTime
                    }
                    lastProgress = currentProgress
                    //回调,更新进度
                    updateProgress(lastProgress, totalBytesRead, contentLength)
                }
                return bytesRead
            }
        }
    }

    private fun updateProgress(progress: Int, currentSize: Long, totalSize: Long) {
        progressCallback.onProgress(progress, currentSize, totalSize)
    }

    //从响应头 Content-Range 中，取 contentLength
    private fun getContentLengthByHeader(response: Response): Long {
        val headerValue = response.header("Content-Range")
        var contentLength: Long = -1
        if (headerValue != null) {
            //响应头Content-Range格式 : bytes 100001-20000000/20000001
            try {
                val divideIndex = headerValue.indexOf("/") //斜杠下标
                val blankIndex = headerValue.indexOf(" ")
                val fromToValue = headerValue.substring(blankIndex + 1, divideIndex)
                val split = fromToValue.split("-".toRegex()).toTypedArray()
                val start = split[0].toLong() //开始下载位置
                val end = split[1].toLong() //结束下载位置
                contentLength = end - start + 1 //要下载的总长度
            } catch (ignore: Exception) {
            }
        }
        return contentLength
    }
}