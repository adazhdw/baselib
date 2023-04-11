package com.adazhdw.ktlib.list.view

import android.view.View
import com.adazhdw.ktlib.list.view.LoadMoreView

interface IWrapperView {
    fun loadMoreView():LoadMoreView
    fun headerView():View
    fun footerView():View
    fun isLoadMoreEnabled(): Boolean

    val headerCount:Int
    val footerCount:Int
    val loadMoreViewCount:Int
    val wrapperCount:Int
    val innerItemCount:Int
    val loadMoreViewPos:Int

    val headerNotEmpty:Boolean
    val footerNotEmpty:Boolean
}