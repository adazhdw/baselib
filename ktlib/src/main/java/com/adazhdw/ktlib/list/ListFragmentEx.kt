package com.adazhdw.ktlib.list

import android.content.Context
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.ktlib.R
import com.adazhdw.ktlib.base.BaseFragmentImpl
import kotlinx.android.synthetic.main.fragment_list_layout_ex.*

abstract class ListFragmentEx<M : Any, A : ListAdapter> : BaseFragmentImpl() {
    override val layoutId: Int
        get() = R.layout.fragment_list_layout_ex
    open val mLoadMoreEnable: Boolean
        get() = true

    private val mListAdapter: A by lazy { onAdapter() }
    private var mCurrentPage: Int = 1
    protected val mHandler: Handler = Handler()
    protected lateinit var mContext: Context

    /**
     * 获取RecyclerView Adapter
     */
    abstract fun onAdapter(): A

    override fun initView(view: View) {
        mContext = view.context
        swipe.setOnRefreshListener {
            refresh()
        }
        listRV.layoutManager = onLayoutManager()
        listRV.addItemDecoration(onItemDecoration())
        listRV.setLoadMoreListener(object : ListRecyclerView.LoadMoreListener {
            override fun onLoadMore() {
                if (mLoadMoreEnable) {
                    requestData(false)
                }
            }
        })
        listRV.loadMoreEnabled(mLoadMoreEnable)
        listRV.adapter = mListAdapter
    }

    override fun requestStart() {
        refresh()
    }

    fun refresh() {
        if (swipe != null) {
            swipe.isRefreshing = true
            requestData(true)
        }
    }

    private fun requestData(isRefresh: Boolean) {
        if (isRefresh) {
            listRV.loadMoreEnabled(false)
        } else {
            swipe.isRefreshing = false
        }
        nextPage(mCurrentPage, object : OnRequestCallback<M> {
            override fun onSuccess(list: List<M>, total: Int) {
                if (list.isNotEmpty())
                    mCurrentPage += 1
                listRV?.loadMoreEnabled(true)
                if (isRefresh) {
                    mCurrentPage = starAtPage()
                    mListAdapter.setData(list)
                    swipe?.isRefreshing = false
                } else {
                    mListAdapter.addData(list)
                }
                val dataEmpty = list.isEmpty()
                val size = mListAdapter.data().size
                val hasMore = size < total
                listRV?.onLoadFinish(dataEmpty, hasMore)
            }

            override fun onError(errorCode: Int, errorMsg: String) {
                swipe?.isRefreshing = false
                listRV?.onLoadError(errorCode, errorMsg)
            }
        })
    }

    abstract fun nextPage(page: Int, callback: OnRequestCallback<M>)

    open fun starAtPage(): Int = 1

    open fun pageSize(): Int = 10

    open fun onItemAnimation(): RecyclerView.ItemAnimator {
        return DefaultItemAnimator()
    }

    open fun onItemDecoration(): RecyclerView.ItemDecoration {
        return BottomItemDecoration(5)
    }

    open fun onLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    protected fun scrollToTop(smooth: Boolean = false) {
        if (smooth) {
            if (mListAdapter.data().isNotEmpty())
                listRV?.smoothScrollToPosition(0)
        } else {
            if (mListAdapter.data().isNotEmpty())
                listRV?.scrollToPosition(0)
        }
    }

    interface OnRequestCallback<M> {
        fun onSuccess(list: List<M>, total: Int)
        fun onError(errorCode: Int, errorMsg: String)
    }
}