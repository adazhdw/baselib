package com.adazhdw.ktlib.base

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

//abstract class CoroutinesActivity : AppCompatActivity(), CoroutineScope by MainScope(){
abstract class CoroutinesActivity : AppCompatActivity(), CoroutineScope {

    private val myJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + myJob


    override fun onDestroy() {
        super.onDestroy()
        myJob.cancel()
    }
}