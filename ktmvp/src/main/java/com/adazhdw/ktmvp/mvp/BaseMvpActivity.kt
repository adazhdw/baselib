package com.adazhdw.ktmvp.mvp

import com.adazhdw.ktlib.base.BaseActivity
import com.adazhdw.ktlib.ext.toast

@Suppress("UNCHECKED_CAST")
abstract class BaseMvpActivity<V : IView, P : IPresenter<V>> : BaseActivity(), IView {

    protected abstract fun obtainPresenter(): P
    protected var mPresenter: P? = null

    override fun initView() {
        mPresenter = obtainPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
        mPresenter = null
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showToast(msg: String?) {
        toast(msg?:"")
    }
}