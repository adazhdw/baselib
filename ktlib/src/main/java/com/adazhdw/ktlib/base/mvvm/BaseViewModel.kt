package com.adazhdw.ktlib.base.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.*

abstract class BaseViewModel<R : BaseRepository> : ViewModel() {

    protected val TAG = this.javaClass.name
    protected val mRepository: R by lazy { obtainRepository() }
    abstract fun obtainRepository(): R

    val netState: MutableLiveData<VMNetState> = MutableLiveData()

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        netState.postValue(NetLoading)
        return launchOnUI(block)
    }

    protected fun launchOnUI(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch {
            tryCatch({
                block()
            }, {
                netState.postValue(NetError(error = it))
                "error:${it.message}".logE(TAG)
            }, {
                netState.postValue(NetSuccess)
            }, true)
        }
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
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
