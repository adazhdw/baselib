package com.adazhdw.ktlib.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.adazhdw.ktlib.base.fragment.ViewBindingFragment
import com.adazhdw.ktlib.databinding.FragmentListLayoutExBinding
import com.adazhdw.ktlib.ext.dp2px
import com.adazhdw.ktlib.list.adapter.BaseVBAdapter
import com.adazhdw.ktlib.list.view.LoadMoreRecyclerView
import com.adazhdw.ktlib.widget.LinearSpacingItemDecoration

/**
 * daguozhu
 * create at 2020/4/13 10:11
 * description:
 */
abstract class ListFragment<T : Any, A : BaseVBAdapter<T>> : ViewBindingFragment() {

    private lateinit var viewBinding: FragmentListLayoutExBinding
    private var currPage = 0
    protected val mDataAdapter: A by lazy { getDataAdapter() }
    protected val mData: List<T>
        get() = mDataAdapter.getData()

    final override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        viewBinding = FragmentListLayoutExBinding.inflate(inflater, container, false)
        return viewBinding
    }

    override fun initView(view: View) {
        viewBinding.swipe.setOnRefreshListener { requestStart() }
        viewBinding.dataRV.setLoadMoreAvailable(loadMoreAvailable())
        viewBinding.dataRV.layoutManager = getLayoutManager()
        viewBinding.dataRV.addItemDecoration(itemDecoration())
        viewBinding.dataRV.adapter = mDataAdapter
        viewBinding.dataRV.setLoadMoreListener(object : LoadMoreRecyclerView.LoadMoreListener {
            override fun onLoadMore() {
                if (loadMoreAvailable()) requestData(false)
            }
        })
        rvExtra(viewBinding.dataRV)
    }

    override fun requestStart() {
        refresh()
    }

    fun refresh() {
        if (!viewBinding.swipe.isRefreshing) {
            viewBinding.swipe.isRefreshing = true
        }
        requestData(viewBinding.swipe.isRefreshing)
    }

    private fun requestData(refreshing: Boolean) {
        if (refreshing) {
            currPage = startAtPage()
            viewBinding.dataRV.setLoadMoreEnabled(false)
            viewBinding.swipe.isEnabled = true
        } else {
            viewBinding.swipe.isEnabled = false
            viewBinding.dataRV.setLoadMoreEnabled(true)
        }
        onLoad(currPage, object : LoadDataCallback<T> {
            override fun onSuccess(data: List<T>, hasMore: Boolean) {
                if (data.isNotEmpty()) currPage += 1
                if (refreshing) {
                    mDataAdapter.setData(data)
                    if (data.isNotEmpty()) {
                        viewBinding.dataRV.scrollToPosition(0)
                    }
                } else {
                    mDataAdapter.addData(data)
                }
                viewBinding.swipe.isEnabled = true
                viewBinding.swipe.isRefreshing = false
                viewBinding.dataRV.setLoadMoreEnabled(true)
                viewBinding.dataRV.loadComplete(hasMore)
            }

            override fun onFail(code: Int, msg: String?) {
                if (refreshing) viewBinding.swipe.isRefreshing = false
                viewBinding.swipe.isEnabled = true
                viewBinding.swipe.isRefreshing = false
                viewBinding.dataRV.setLoadMoreEnabled(true)
                viewBinding.dataRV.loadComplete(hasMore = true, error = true)
                onError(code, msg)
            }
        })
    }

    abstract fun onLoad(page: Int, callback: LoadDataCallback<T>)
    abstract fun getDataAdapter(): A
    open fun loadMoreAvailable(): Boolean = true/*总开关，控制loadMore是否可用*/
    open fun startAtPage() = 0/*开始页数*/
    open fun perPage() = 20/*每页个数pageSize*/
    open fun onError(code: Int, msg: String?) {}
    open fun rvExtra(recyclerView: LoadMoreRecyclerView) {}/*recyclerView其他属性设置*/
    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    open fun itemDecoration(): RecyclerView.ItemDecoration {
        return LinearSpacingItemDecoration(dp2px(15f), LinearLayoutManager.VERTICAL, true)
    }

    interface LoadDataCallback<T> {
        fun onSuccess(data: List<T>, hasMore: Boolean)
        fun onFail(code: Int, msg: String?)
    }

}