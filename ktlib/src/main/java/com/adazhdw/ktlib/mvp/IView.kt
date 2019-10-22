package com.adazhdw.ktlib.mvp

interface IView {

    /**
     * 方便识别类名Log
     */
    fun tag():String

    /**
     * 显示loading
     */
    fun showLoading()

    /**
     * 隐藏Loading
     */
    fun hideLoading()

    /**
     * 显示Toast信息
     */
    fun showToast(msg: String?)
}