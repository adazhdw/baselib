package com.adazhdw.baselibrary.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseModel :IModel,LifecycleObserver {
    val TAG:String = this.javaClass.simpleName

    override fun tag(): String {
        return TAG
    }

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun addDisposable(disposable: Disposable?) {
        if (mCompositeDisposable==null){
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let{
            mCompositeDisposable?.add(it)
        }
    }

    override fun onDetach() {
        unDispose()
    }

    private fun unDispose() {
        mCompositeDisposable?.clear()  // 保证Activity结束时取消
        mCompositeDisposable = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner){
        owner.lifecycle.removeObserver(this)
    }

}