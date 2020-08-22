package com.adazhdw.ktlib.base.mvvm

import androidx.lifecycle.Observer
import com.adazhdw.ktlib.base.activity.BaseActivity

abstract class BaseVMActivity<R : BaseRepository, VM : BaseViewModel<R>> : BaseActivity() {

    abstract val viewModel: VM

    override fun initData() {
        viewModel.netState.observe(this, Observer {
            when (it) {
                is NetLoading -> showLoading()
                is NetSuccess -> hideLoading()
                is NetError -> showError(it)
            }
        })
    }

    abstract fun showLoading()
    abstract fun hideLoading()
    abstract fun showError(netError: NetError)

}