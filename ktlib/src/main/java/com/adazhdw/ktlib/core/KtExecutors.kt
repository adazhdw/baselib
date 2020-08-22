package com.adazhdw.ktlib.core

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

object KtExecutors {

    val diskIO: ExecutorService = Executors.newSingleThreadExecutor()

    val networkIO: ThreadPoolExecutor = Executors.newFixedThreadPool(3) as ThreadPoolExecutor

    val mainThread = MainThreadExecutor()

    class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }
}
