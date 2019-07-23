package com.adazhdw.baselibrary.http

import com.adazhdw.baselibrary.LibUtil
import com.adazhdw.baselibrary.R
import com.adazhdw.baselibrary.ext.logD
import com.adazhdw.baselibrary.ext.logE
import com.adazhdw.baselibrary.mvp.IModel
import com.adazhdw.baselibrary.mvp.IView
import com.adazhdw.baselibrary.utils.NetworkUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * RxJava常用实践
 * 1、有一些操作不想写在业务层，而是想做一个统一的处理。
 * 更直白的说法是 : 我的统一操作不依赖于特定的数据类型，而只需要一些共有的参数
 * 这时候可以使用doOnNext()
 */
//Consumer
fun <T> Observable<T>.subsC(
    model: IModel? = null,
    view: IView? = null,
    isShowLoading: Boolean = true,
    onSuccess: ((T) -> Unit),
    onFail: ((Throwable) -> Unit)? = null,
    doOnNextFun: ((T) -> Unit)? = null
) {
    if (isShowLoading) {
        view?.showLoading()
    }
    if (!NetworkUtil.isConnected) {
        view?.showToast(LibUtil.getApp().getString(R.string.net_work_unavailable))
        view?.hideLoading()
    }
    val disposable = this.doOnNext {
        doOnNextFun?.invoke(it)
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            onSuccess(it)
            view?.hideLoading()
            logD(view?.tag(), "onSuccess")
        }, { ex ->
            onFail?.invoke(ex)
            view?.hideLoading()
            view?.showToast(ex.message)
            logE(view?.tag(), "onFail---${ex.message}")
        }, {
            view?.hideLoading()
            logD(view?.tag(), "onComplete")
        })
    model?.addDisposable(disposable)
    logD(view?.tag(), "addDisposable")
}

//Observer
fun <T> Observable<T>.subsO(
    model: IModel?,
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: ((T) -> Unit),
    onFail: ((Throwable) -> Unit)? = null,
    doOnNextFun: ((T) -> Unit)? = null
) {
    if (!NetworkUtil.isConnected) {
        view?.showToast(LibUtil.getApp().getString(R.string.net_work_unavailable))
        view?.hideLoading()
    }
    this.doOnNext {
        doOnNextFun?.invoke(it)
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Observer<T> {
            override fun onComplete() {
                view?.hideLoading()
                logD(view?.tag(), "onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                if (isShowLoading) {
                    view?.showLoading()
                }
                model?.addDisposable(d)
                logD(view?.tag(), "addDisposable")
                if (!NetworkUtil.isConnected) {
                    view?.showToast(LibUtil.getApp().getString(R.string.net_work_unavailable))
                    onComplete()
                }
            }

            override fun onNext(t: T) {
                onSuccess(t)
                logD(view?.tag(), "onSuccess")
            }

            override fun onError(e: Throwable) {
                onFail?.invoke(e)
                view?.hideLoading()
                view?.showToast(e.message)
                logE(view?.tag(), "onError---${e.message}")
            }

        })
}
