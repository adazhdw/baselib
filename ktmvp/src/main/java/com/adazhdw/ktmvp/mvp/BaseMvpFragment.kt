package com.adazhdw.ktmvp.mvp

import android.view.View
import com.adazhdw.ktlib.base.BaseFragment
import com.adazhdw.ktlib.ext.toast

@Suppress("UNCHECKED_CAST")
abstract class BaseMvpFragment<V : IView, P : IPresenter<V>> : BaseFragment(), IView {

    protected abstract fun obtainPresenter(): P

    protected var mPresenter: P? = null

    override fun initView(view: View) {
        mPresenter = obtainPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
        this.mPresenter = null
    }

    override fun initData() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showToast(msg: String?) {
        toast(msg ?: "")
    }

}