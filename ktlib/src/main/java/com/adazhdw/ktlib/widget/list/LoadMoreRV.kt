package com.adazhdw.ktlib.widget.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * Administrator
 * create at 2020/4/8 16:20
 * description:
 */
class LoadMoreRV : RecyclerView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        resolveState()
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        mScrollState = state
        if (mScrollState != SCROLL_STATE_IDLE) return
        resolveState()
    }

    private fun resolveState() {
        if (mScrollState != SCROLL_STATE_IDLE || !this.loadMoreEnabled) return
        val layoutManager = layoutManager
        val itemCount = layoutManager?.itemCount ?: 0
        if (itemCount <= 0) return
        val lastVisiblePosition: Int
        when (layoutManager) {
            is GridLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (itemCount == lastVisiblePosition + 1) dispatchLoadMore()
            }
            is StaggeredGridLayoutManager -> {
                val into = intArrayOf(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(into)
                lastVisiblePosition = into.max() ?: 0
                if (itemCount == lastVisiblePosition + 1) dispatchLoadMore()
            }
            is LinearLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (itemCount == lastVisiblePosition + 1) dispatchLoadMore()
            }
        }
    }

    private var mScrollState = -1
    private var loadMoreEnabled = true
    private var isLoadingMore = false

    private fun dispatchLoadMore() {
        if (!this.isLoadingMore) {
            isLoadingMore = true
            val adapter = adapter
            if (adapter != null && adapter is LoadMoreAdapter<*>) {
                adapter.loading()
            }
            mLoadMoreListener?.loadMore()
        }
    }

    fun setLoadMoreEnabled(enabled: Boolean) {
        this.loadMoreEnabled = enabled
    }

    fun setIsLoading(loading: Boolean) {
        this.isLoadingMore = loading
    }

    private var mLoadMoreListener: LoadMoreListener? = null

    fun setLoadMoreListener(listener: LoadMoreListener) {
        mLoadMoreListener = listener
    }

    interface LoadMoreListener {
        fun loadMore()
    }
}