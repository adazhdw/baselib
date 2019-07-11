package com.adazhdw.baselibrary.mvp

interface IActivity<V:IView> {
    fun obtaionPresenter():IPresenter<V>
}