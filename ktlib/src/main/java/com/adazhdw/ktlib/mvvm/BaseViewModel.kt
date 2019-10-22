package com.adazhdw.ktlib.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adazhdw.ktlib.ext.loge

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel<R : BaseRepository> : ViewModel() {

    protected val mRepository: R by lazy { obtainRepository() }

    abstract fun obtainRepository(): R

    fun launch(block: suspend () -> Unit) {
        launchOnUI(block, {
            errorFun(it)
        })
    }

    open fun errorFun(throwable: Throwable) {
        loge(content = throwable.message)
    }

    private fun launchOnUI(block: suspend () -> Unit, error: suspend (Throwable) -> Unit): Job =
            viewModelScope.launch {
                try {
                    block()
                } catch (ex: Throwable) {
                    error(ex)
                }
            }

}
