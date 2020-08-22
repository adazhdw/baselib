@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext.recycler

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

fun RecyclerView.isSlideToBottom(): Boolean {
    return computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange()
}

fun RecyclerView.isAlreadyBottom(): Boolean {
    return !canScrollVertically(1)
}

fun RecyclerView.isScrollToTop(): Boolean {
    return canScrollVertically(-1)//的值表示是否能向下滚动，false表示已经滚动到顶部
}

fun Context.gridLayoutManager(spanCount: Int) =
    GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false)

fun Context.linearLayoutManager() = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

fun Context.staggeredGridLayoutManager(spanCount: Int) =
    StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
