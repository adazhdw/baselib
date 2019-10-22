package com.adazhdw.ktlib.base

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class CoroutinesFragment : Fragment(), CoroutineScope {


    private val myJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + myJob

    override fun onDestroyView() {
        super.onDestroyView()
        myJob.cancel()
    }
}