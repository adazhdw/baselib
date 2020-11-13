package com.adazhdw.ktlib.kthttp.progress.body

import com.adazhdw.ktlib.kthttp.progress.ProgressCallback
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * author：daguozhu
 * date-time：2020/9/2 10:22
 * description：
 **/
class ProgressRequestBody(
    private val requestBody: RequestBody,
    private val progressCallback: ProgressCallback
) : RequestBody() {

    companion object {
        private const val MIN_INTERVAL = 50
    }

    //包装完成的BufferedSink
    private var bufferedSink: BufferedSink? = null

    override fun contentLength(): Long = requestBody.contentLength()

    override fun contentType(): MediaType? = requestBody.contentType()

    override fun writeTo(sink: BufferedSink) {
        if (sink is Buffer) return
        if (bufferedSink == null) {
            bufferedSink = sink(sink).buffer()
        }
        //写入
        requestBody.writeTo(bufferedSink!!)
        bufferedSink?.flush()
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            var bytesWritten = 0L

            //总字节长度，避免多次调用contentLength()方法
            var contentLength = 0L
            var lastProgress //上次回调进度
                    = 0
            var lastTime //上次回调时间
                    : Long = 0

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                val currentProgress = (bytesWritten * 100 / contentLength).toInt()
                if (currentProgress <= lastProgress) return  //进度较上次没有更新，直接返回
                //当前进度小于100,需要判断两次回调时间间隔是否小于一定时间,是的话直接返回
                if (currentProgress < 100) {
                    val currentTime = System.currentTimeMillis()
                    //两次回调时间间隔小于 MIN_INTERVAL 毫秒,直接返回,避免更新太频繁
                    if (currentTime - lastTime < MIN_INTERVAL) return
                    lastTime = currentTime
                }
                lastProgress = currentProgress
                //回调, 更新进度
                updateProgress(lastProgress, bytesWritten, contentLength)
            }
        }
    }

    private fun updateProgress(progress: Int, currentSize: Long, totalSize: Long) {
        progressCallback.onProgress(progress, currentSize, totalSize)
    }
}