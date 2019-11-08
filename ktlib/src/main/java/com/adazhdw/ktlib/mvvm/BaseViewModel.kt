package com.adazhdw.ktlib.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adazhdw.ktlib.ext.logE
import com.adazhdw.ktlib.ext.loge
import kotlinx.coroutines.*

abstract class BaseViewModel<R : BaseRepository> : ViewModel() {

    protected val TAG = this.javaClass.simpleName
    protected val mRepository: R by lazy { obtainRepository() }

    abstract fun obtainRepository(): R

    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return launchOnUI(block)
    }

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch {
            tryCatch(block, { "error:${it.message}".logE(TAG) }, {}, true)
        }
    }

    private suspend fun tryCatch(
            tryBlock: suspend CoroutineScope.() -> Unit,
            catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
            finallyBlock: suspend CoroutineScope.() -> Unit,
            handleCancellationExceptionManually: Boolean = false) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    catchBlock(e)
                } else {
                    throw e
                }
            } finally {
                finallyBlock()
            }
        }
    }

}
