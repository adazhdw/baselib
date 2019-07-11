package com.adazhdw.baselibrary.base.mvvm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.adazhdw.baselibrary.ext.logE
import com.adazhdw.baselibrary.ext.toast
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
        toast(throwable.message)
        logE(throwable)
    }

    private fun launchOnUI(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (ex: Throwable) {
            error(ex)
        }
    }

}

fun <T : ViewModel> Fragment.getViewModel(modelClass: Class<T>): T {
    return ViewModelProviders.of(this).get(modelClass)
}

fun <T : ViewModel> FragmentActivity.getViewModel(modelClass: Class<T>): T {
    return ViewModelProviders.of(this).get(modelClass)
}