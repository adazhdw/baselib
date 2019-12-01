package com.adazhdw.ktlib.list

import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.ktlib.R
import com.adazhdw.ktlib.base.BaseFragment
import com.adazhdw.ktlib.widget.LinearSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_list_layout_ex.*

abstract class ListFragmentEx<M : Any, A : ListAdapter> : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_list_layout_ex
    open val mLoadMoreEnable: Boolean
        get() = true

    private val mListAdapter: A by lazy { onAdapter() }
    private var mCurrentPage: Int = 1
    protected val mHandler: Handler = Handler()

    /**
     * 获取RecyclerView Adapter
     */
    abstract fun onAdapter(): A

    override fun initView(view: View) {
        swipe.setOnRefreshListener {
            refresh()
        }
        mListAdapter.mContext = view.context
        listRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listRV.addItemDecoration(onItemDecoration())
        listRV.setLoadMoreListener(object : ListRecyclerView.LoadMoreListener {
            override fun onLoadMore() {
                requestData(false)
            }
        })
        val loadMoreView = DefaultLoadMoreView(view.context)
        listRV.setLoadMoreView(loadMoreView)
        mListAdapter.setLoadMoreView(loadMoreView)
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
            mCurrentPage = starAtPage()
            listRV.loadMoreEnabled(false)
        } else {
            swipe.isRefreshing = false
        }
        nextPage(mCurrentPage, object : OnRequestCallback<M> {
            override fun onSuccess(list: List<M>) {
                mCurrentPage += 1
                listRV?.loadMoreEnabled(true)
                if (isRefresh) {
                    mListAdapter.setData(list)
                    swipe?.isRefreshing = false
                } else {
                    mListAdapter.addData(list)
                    listRV?.onLoadFinish(list.isEmpty(), true)
                }
                mListAdapter.loading(false)
            }

            override fun onError(errorCode: Int, errorMsg: String) {
                swipe?.isRefreshing = false
                listRV?.onLoadError(errorCode, errorMsg)
                mListAdapter.loading(false)
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

    interface OnRequestCallback<M> {
        fun onSuccess(list: List<M>)
        fun onError(errorCode: Int, errorMsg: String)
    }

    override fun initData() {

    }

}