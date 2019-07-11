package com.adazhdw.baselibrary.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

abstract class BasePresenter<M : IModel, V : IView> : IPresenter<V>, LifecycleObserver {
    val TAG: String = this.javaClass.simpleName

    override fun tag(): String {
        return TAG
    }

    protected var mModel: M? = null
    protected var mView: V? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(mView: V) {
        this.mView = mView
        mModel = obtainModel()
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun detachView() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        // 保证activity结束时取消所有正在执行的订阅
        unDispose()
        mModel?.onDetach()
        this.mModel = null
        this.mView = null
        this.mCompositeDisposable = null
    }

    open fun addDispose(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    private fun unDispose() {
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
    }

    /**
     * 创建Model
     */
    abstract fun obtainModel(): M

    /**
     * 是否使用EventBus
     */
    open fun useEventBus(): Boolean {
        return false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }

}