package com.adazhdw.baselibrary.ext

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.isSlideToBottom(): Boolean {
    return computeVerticalScrollExtent() + computeVerticalScrollOffset() >= computeVerticalScrollRange()
}

fun RecyclerView.isAlreadyBottom(): Boolean {
    return !canScrollVertically(1)
}

fun RecyclerView.isScrollToBottom(): Boolean {
    return canScrollVertically(1)//的值表示是否能向上滚动，false表示已经滚动到底部
}

fun RecyclerView.isScrollToTop(): Boolean {
    return canScrollVertically(-1)//的值表示是否能向下滚动，false表示已经滚动到顶部
}
