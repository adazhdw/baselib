package com.adazhdw.kthttp.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

object KtExecutors {

    private const val THREAD_COUNT = 3

    val diskIO: Executor = DiskIOThreadExecutor()

    val networkIO: ThreadPoolExecutor =
        Executors.newFixedThreadPool(THREAD_COUNT) as ThreadPoolExecutor

    val mainThread = MainThreadExecutor()

    class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    class DiskIOThreadExecutor : Executor {

        private val diskIO = Executors.newSingleThreadExecutor()

        override fun execute(command: Runnable) {
            diskIO.execute(command)
        }
    }
}
