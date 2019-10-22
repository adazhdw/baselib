package com.adazhdw.ktlib.mvp

import io.reactivex.disposables.Disposable

interface IModel {
    /**
     * 方便识别类名Log
     */
    fun tag(): String

    fun addDisposable(disposable: Disposable?)

    fun onDetach()

}