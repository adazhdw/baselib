package com.adazhdw.ktlib.list.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


internal const val VIEW_TYPE_HEAD = 20000001
internal const val VIEW_TYPE_FOOTER = 20000002
internal const val VIEW_TYPE_LOAD_MORE = 20000003

class LoadMoreRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RecyclerView(context, attrs, defStyleAttr), IWrapperView {

    private val mHandler = Handler(Looper.getMainLooper())
    private var isLoading = false//是否正在进行网络请求
    private var hasMoreData = true// 是否有更多数据
    private var isLoadMoreEnabled = false//总开关，控制loadMore是否可用
    private var isLoadMoreGone = false//loadMoreView是否显示在底部
    private var mLoadMoreListener: LoadMoreListener? = null
    private var mWrapAdapter: WrapperAdapter? = null
    private var mDataObserver: DataObserver? = null
    private var loadMoreView: LoadMoreView
    private val footerView: LinearLayout
    private val headerView: LinearLayout

    private val alreadyTopOrBottom: Boolean
        get() = !canScrollVertically(1) || !canScrollVertically(-1)

    init {
        loadMoreView = LoadMoreViewImpl(context)
        footerView = LinearLayout(context)
        footerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        headerView = LinearLayout(context)
        headerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return super.canScrollVertically(direction) && !isLoading()
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        /*  state：
            SCROLL_STATE_IDLE     = 0 ：静止,没有滚动
            SCROLL_STATE_DRAGGING = 1 ：正在被外部拖拽,一般为用户正在用手指滚动
            SCROLL_STATE_SETTLING = 2 ：自动滚动开始

            RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
            RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        */
        // 判断RecyclerView滚动到底部，参考：http://www.jianshu.com/p/c138055af5d2
        if (state != SCROLL_STATE_IDLE) return
        val layoutManager = layoutManager
        val itemCount = (layoutManager?.itemCount ?: 0) + wrapperCount
        if (itemCount <= 0) return
        val lastVisiblePosition: Int
        var canLoadMore = false
        when (layoutManager) {
            is GridLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (itemCount - 1 == lastVisiblePosition) canLoadMore = true
            }
            is StaggeredGridLayoutManager -> {
                val into = intArrayOf(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(into)
                lastVisiblePosition = into.maxOrNull() ?: 0
                if (itemCount - 1 == lastVisiblePosition) canLoadMore = true
            }
            is LinearLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (itemCount - 1 == lastVisiblePosition) canLoadMore = true
            }
        }
        if (isLoadMoreEnabled && canLoadMore && alreadyTopOrBottom && hasMoreData && !isLoading) {
            doLoadMore()
        }
    }

    private fun doLoadMore() {
        this.isLoading = true
        this.hasMoreData = true
        this.loadMoreView.loading()
        this.mLoadMoreListener?.onLoadMore()
    }

    fun setLoadMoreEnabled(enabled: Boolean) {
        val oldLoadMoreCount = loadMoreViewCount
        this.isLoadMoreEnabled = enabled
        val newLoadMoreCount = loadMoreViewCount
        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                this.mWrapAdapter?.notifyItemRemoved(loadMoreViewPos)
            }
        } else if (newLoadMoreCount == 1) {
            this.mWrapAdapter?.notifyItemInserted(loadMoreViewPos)
        }
    }

    fun loadComplete(hasMore: Boolean, error: Boolean = false) {
        this.isLoading = false
        this.hasMoreData = hasMore
        if (error) {
            this.loadMoreView.loadError()
        } else {
            this.loadMoreView.loadSuccess()
        }
        this.mWrapAdapter?.notifyItemChanged(loadMoreViewPos)
    }

    fun loadEnd(gone: Boolean = false) {
        mHandler.post {
            if (loadMoreViewCount != 0) {
                this.isLoading = false
                this.hasMoreData = false
                this.isLoadMoreGone = gone
                this.loadMoreView.loadSuccess()
                if (gone) {
                    this.mWrapAdapter?.notifyItemRemoved(loadMoreViewPos)
                } else {
                    this.mWrapAdapter?.notifyItemChanged(loadMoreViewPos)
                }
            }
        }
    }

    fun setLoadMoreListener(listener: LoadMoreListener) {
        this.mLoadMoreListener = listener
        this.setLoadMoreEnabled(true)
    }

    fun setLoadMoreView(loadMoreView: LoadMoreView) {
        this.loadMoreView = loadMoreView
    }

    fun addFooterView(footer: View, heightPx: Int = 0) {
        var height = heightPx
        if (heightPx == 0) {
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        footerView.addView(footer, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height))
    }

    fun addHeaderView(header: View, heightPx: Int = 0) {
        var height = heightPx
        if (heightPx == 0) {
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        headerView.addView(header, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height))
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }

    //------------------------------------------------
    //------ Adapter 相关
    //------------------------------------------------

    @Suppress("UNCHECKED_CAST")
    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter == null) return
        mWrapAdapter = WrapperAdapter(adapter as Adapter<ViewHolder>, this)
        if (mDataObserver != null) {
            // 为原有的RecyclerAdapter移除数据监听对象
            adapter.unregisterAdapterDataObserver(mDataObserver!!)
        }
        if (mDataObserver == null) {
            mDataObserver = DataObserver(mWrapAdapter!!)
            // 为原有的RecyclerAdapter添加数据监听对象
            adapter.registerAdapterDataObserver(mDataObserver!!)
            mDataObserver!!.onChanged()
        }
        super.setAdapter(mWrapAdapter)
    }

    override fun getAdapter(): Adapter<*>? {
        mWrapAdapter?.let {
            return it.innerAdapter
        } ?: return super.getAdapter()
    }

    override fun loadMoreView(): LoadMoreView = loadMoreView
    override fun headerView(): View = headerView
    override fun footerView(): View = footerView
    override fun isLoadMoreEnabled(): Boolean = isLoadMoreEnabled
    override fun isLoading(): Boolean = isLoading
    override val headerNotEmpty: Boolean get() = headerView.isNotEmpty()
    override val footerNotEmpty: Boolean get() = footerView.isNotEmpty()
    override val headerCount: Int get() = if (headerNotEmpty) 1 else 0
    override val footerCount: Int get() = if (footerNotEmpty) 1 else 0
    override val wrapperCount: Int get() = headerCount + footerCount + loadMoreViewCount
    override val innerItemCount: Int get() = this.mWrapAdapter?.innerItemCount() ?: 0
    override val loadMoreViewPos: Int get() = headerCount + innerItemCount + footerCount
    override val loadMoreViewCount: Int
        get() = if (isLoadMoreEnabled && mLoadMoreListener != null) {
            if (isLoadMoreGone || innerItemCount == 0) 0 else 1
        } else 0


}