package com.adazhdw.ktmvp.mvp

abstract class BaseView : IView {
    val TAG:String = this.javaClass.simpleName

    override fun tag(): String {
        return TAG
    }

    override fun showLoading() {

    }
    override fun hideLoading() {

    }

    override fun showToast(msg: String?) {

    }

}