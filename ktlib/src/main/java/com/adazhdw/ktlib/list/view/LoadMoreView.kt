package com.adazhdw.ktlib.list.view

import android.view.View

interface LoadMoreView {

    fun loadSuccess()

    fun loadError()

    fun loading()

    fun getContentView(): View

}
