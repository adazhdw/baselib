package com.adazhdw.net

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.Buffer
import java.io.IOException
import java.net.URLConnection
import java.util.*
import java.util.concurrent.Executor

class Utils {
    companion object {
        @Throws(IOException::class)
        @JvmStatic
        fun buffer(body: ResponseBody): ResponseBody {
            val buffer = Buffer()
            body.source().readAll(buffer)
            return buffer.asResponseBody(body.contentType(), body.contentLength())
        }

        // https://github.com/ReactiveX/RxJava/blob/6a44e5d0543a48f1c378dc833a155f3f71333bc2/
        // src/main/java/io/reactivex/exceptions/Exceptions.java#L66
        @JvmStatic
        fun throwIfFatal(t: Throwable?) {
            when (t) {
                is VirtualMachineError -> {
                    throw t
                }
                is ThreadDeath -> {
                    throw t
                }
                is LinkageError -> {
                    throw t
                }
            }
        }

        @JvmStatic
        fun guessMimeType(fileName: String): okhttp3.MediaType {
            var name = fileName
            val fileNameMap = URLConnection.getFileNameMap()
            name = name.replace("#", "")//解决文件名中含有#号异常的问题
            val contentType = fileNameMap.getContentTypeFor(name) ?: return HttpHeaders.MEDIA_TYPE_STREAM
            return contentType.toMediaType()
        }
    }

}

open class Platform private constructor() {
    companion object {
        internal val PLATFORM: Platform by lazy { findPlatform() }

        private fun findPlatform(): Platform {
            return when (System.getProperty("java.vm.name")) {
                "Dalvik" -> Android()
                else -> Platform()
            }
        }
    }

    open fun defaultCallbackExecutor(): Executor? {
        return null
    }

    open fun defaultCallAdapterFactories(callbackExecutor: Executor): List<CallAdapter.Factory> {
        return Collections.singletonList(DefaultCallAdapterFactory(callbackExecutor))
    }

    open fun defaultCallAdapterFactoriesSize() = 1

    open fun defaultConverterFactories(): List<Converter.Factory> {
        return emptyList()
    }

    open fun defaultConverterFactoriesSize() = 0

    class Android : Platform() {
        override fun defaultCallbackExecutor(): Executor {
            return ExecutorUtils.mainThread
        }
    }
}

