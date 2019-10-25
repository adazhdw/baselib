package com.adazhdw.ktlib.list

import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adazhdw.ktlib.R
import com.adazhdw.ktlib.base.BaseFragmentImpl
import com.adazhdw.ktlib.ext.view.invisible
import com.adazhdw.ktlib.ext.view.layoutParamsWrap
import com.adazhdw.ktlib.ext.recycler.isSlideToBottom
import com.adazhdw.ktlib.ext.view.isVisible
import com.adazhdw.ktlib.ext.view.visible
import com.blankj.utilcode.util.SizeUtils
import kotlinx.android.synthetic.main.fragment_list_line.*

/**
 * ViewModel 模式并不适合目前的ListFragment的加载模式
 *
 * ViewModel mode isn't suitable for the current Loading data mode of the ListFragment
 */
abstract class ListFragmentLine<M, A : BaseRvAdapter<M>> : BaseFragmentImpl(),
    SwipeRefreshLayout.OnRefreshListener {
    private val mListAdapter by lazy { onAdapter() }
    private val mEmptyView: View by lazy { onEmptyView() }
    private var currPage = 0

    protected val mHandler = Handler()
    protected val listSize: Int
        get() = mListAdapter.mData.size
    protected val list: List<M>
        get() = mListAdapter.mData

    override val layoutId: Int
        get() = R.layout.fragment_list_line

    protected abstract val mLoadMoreEnable: Boolean

    override fun initView(view: View) {
        onListHeader(swipe)
        lineRecyclerView.itemAnimator = onItemAnimator()
        lineRecyclerView.layoutManager = onLayoutManager()
        lineRecyclerView.addItemDecoration(onItemDecoration())
        lineRecyclerView.adapter = mListAdapter.apply { mContext = view.context }
        lineRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.isSlideToBottom()) {
                    if (mLoadMoreEnable)
                        mHandler.post {
                            nextPage()
                        }
                }
            }
        })
        swipe.setOnRefreshListener(this)

        initEmptyView()
    }

    private fun initEmptyView() {
        rooContentFl.addView(mEmptyView, rooContentFl.layoutParamsWrap())
        mEmptyView.invisible()
    }

    override fun requestStart() {
        refresh()
    }

    /**
     * 刷新页面
     */
    fun refresh() {
        if (swipe != null) {
            swipe.isRefreshing = true
            onRefresh()
        }
    }


    protected fun getListItem(position: Int): M {
        return mListAdapter.mData[position]
    }

    /**
     * none data tip text
     */
    protected fun noDataTip(): String {
        return getString(R.string.no_more_data_text)
    }

    /**
     * SwipeRefreshLayout style define
     */
    protected open fun onListHeader(mHeader: SwipeRefreshLayout) {

    }

    /**
     * network request pageSize
     */
    protected open fun pageSize(): Int {
        return 10
    }

    /**
     * network request start at [pageStartAt]
     */
    protected open fun pageStartAt(): Int {
        return 1
    }

    protected open fun onLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    protected open fun onItemDecoration(): RecyclerView.ItemDecoration {
        return BottomItemDecoration(SizeUtils.dp2px(10F))
    }

    protected open fun onItemAnimator(): RecyclerView.ItemAnimator {
        return DefaultItemAnimator()
    }

    /**
     * return recyclerView adapter
     */
    protected abstract fun onAdapter(): A

    /**
     * return list empty view
     */
    protected abstract fun onEmptyView(): View

    /**
     * network request
     */
    protected abstract fun onNextPage(page: Int, callback: LoadCallback)

    override fun onRefresh() {
        mListAdapter.isRefresh = true
        nextPage()
    }

    private fun nextPage() {
        if (mListAdapter.isLoading)
            return
        mListAdapter.isLoading = true

        val refresh = swipe.isRefreshing
        currPage = pageStartAt() + if (refresh) 0 else currPage
        if (!refresh) {
            swipe.isEnabled = false
            mListAdapter.showLoading()
            mListAdapter.isRefresh = false
        } else {
            mListAdapter.isRefresh = true
        }

        onNextPage(currPage, object : LoadCallback() {
            override fun onResult() {
                if (refresh) {
                    mListAdapter.clearData()
                }
                mListAdapter.isLoading = false
                mListAdapter.isRefresh = false
                swipe?.isEnabled = true
                swipe?.isRefreshing = false
                mListAdapter.showLoading()
            }

            override fun onSuccessLoad(list: List<M>) {
                if (list.isNotEmpty()) {
                    if (mEmptyView.isVisible)
                        mEmptyView.invisible()
                    if (lineRecyclerView?.isInvisible == true)
                        lineRecyclerView?.visible()
                    mListAdapter.mData = list as MutableList<M>
                } else {
                    if (mListAdapter.mData.isEmpty()) {
                        mEmptyView.visible()
                        lineRecyclerView?.invisible()
                    } else {
                        mEmptyView.invisible()
                        lineRecyclerView.visible()
                    }
                    Toast.makeText(context, noDataTip(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    abstract inner class LoadCallback {
        /**
         * must invoke this method to end load progress
         */
        abstract fun onResult()

        /**
         * Call this method means load data success
         * thi method must after [.onResult]，and between two method mustn't have [ListFragmentLine.nextPage] call
         */
        abstract fun onSuccessLoad(list: List<M>)
    }

}
