package com.adazhdw.baselibrary.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class CoroutinesActivity : AppCompatActivity(), CoroutineScope {

    private val myViewModel = BaseViewModelImpl()
    override val coroutineContext: CoroutineContext
        get() = myViewModel.viewModelScope.coroutineContext


    override fun onDestroy() {
        super.onDestroy()
        myViewModel.viewModelScope.cancel()
    }
}