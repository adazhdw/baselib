package com.adazhdw.ktlib.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent

interface IPresenter<V : IView> {
    /**
     * 方便识别类名Log
     */
    fun tag(): String

    /**
     * 绑定View
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun attachView(mView: V)

    /**
     * 解绑View
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun detachView()
}