package com.adazhdw.ktlib.base.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adazhdw.ktlib.ext.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel<R : BaseRepository> : ViewModel() {

    protected val TAG = this.javaClass.name
    protected val mRepository: R by lazy { obtainRepository() }
    abstract fun obtainRepository(): R

    val netState: MutableLiveData<VMNetState> = MutableLiveData()

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        netState.postValue(Loading)
        return viewModelScope.launch {
            runCatching {
                block()
            }.onSuccess {
                netState.postValue(Success)
            }.onFailure {
                netState.postValue(Error(error = it))
                "error:${it.message}".logE(TAG)
            }
        }
    }
}
