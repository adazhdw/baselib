package com.adazhdw.ktlib.mvp

interface IActivity<V:IView> {
    fun obtaionPresenter():IPresenter<V>
}