package com.adazhdw.ktlib.base

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class CoroutinesActivity : AppCompatActivity(), CoroutineScope {

    private val myJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + myJob


    override fun onDestroy() {
        super.onDestroy()
        myJob.cancel()
    }
}