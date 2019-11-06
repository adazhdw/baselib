package com.adazhdw.ktmvp.mvp

interface IActivity<V: IView> {
    fun obtaionPresenter(): IPresenter<V>
}