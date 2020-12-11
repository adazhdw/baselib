package com.adazhdw.ktlib.widget.list

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * author：daguozhu
 * date-time：2020/12/10 15:26
 * description：只实现滑动到底部或者顶部loadMore方法的判断，
 *              footer或者header、loadMoreView使用 外部Adapter实现
 **/
class LoadMoreRVEx : RecyclerView {

    companion object {
        const val SCROLL_DIRECTION_TOP = -1
        const val SCROLL_DIRECTION_BOTTOM = 1
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    @IntDef(SCROLL_DIRECTION_TOP, SCROLL_DIRECTION_BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class ScrollDirection {}

    private var isLoading = false//是否正在进行网络请求
    private var hasMore = true// 是否有更多数据
    private var mLoadMoreEnabled = false// loadMore 是否可用
    private var mLoadMoreListener: LoadMoreListener? = null
    private var mWrapAdapter: WrapAdapter<*>? = null
    private val mDataObserver: DataObserver = DataObserver()
    private var mScrollDirection: Int = SCROLL_DIRECTION_BOTTOM

    private fun initView() {

    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        /*  state：
            SCROLL_STATE_IDLE     = 0 ：静止,没有滚动
            SCROLL_STATE_DRAGGING = 1 ：正在被外部拖拽,一般为用户正在用手指滚动
            SCROLL_STATE_SETTLING = 2 ：自动滚动开始
        */
        /*
            RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
            RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        */
        // 判断RecyclerView滚动到底部，参考：http://www.jianshu.com/p/c138055af5d2
        if (state != SCROLL_STATE_IDLE) return
        val layoutManager = layoutManager
        val itemCount = layoutManager?.itemCount ?: 0
        if (itemCount <= 0) return
        val lastVisiblePosition: Int
        var canLoadMore = false
        when (layoutManager) {
            is GridLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (itemCount == lastVisiblePosition + 1) canLoadMore = true
            }
            is StaggeredGridLayoutManager -> {
                val into = intArrayOf(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(into)
                lastVisiblePosition = into.maxOrNull() ?: 0
                if (itemCount == lastVisiblePosition + 1) canLoadMore = true
            }
            is LinearLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (itemCount == lastVisiblePosition + 1) canLoadMore = true
            }
        }
        if (canLoadMore && state == SCROLL_STATE_IDLE && alreadyTopOrBottom() && mLoadMoreEnabled && hasMore && !isLoading) {
            doLoadMore()
        }
    }

    private fun alreadyTopOrBottom() = !canScrollVertically(mScrollDirection)

    private fun doLoadMore() {
        this.isLoading = true
        this.hasMore = true
        this.mLoadMoreListener?.onLoadMore()
    }

    fun loadComplete(hasMore: Boolean, error: Boolean = false) {
        this.isLoading = false
        this.hasMore = hasMore
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter == null) return
        mWrapAdapter = WrapAdapter(adapter)
        adapter.registerAdapterDataObserver(mDataObserver)
        mDataObserver.onChanged()
        super.setAdapter(mWrapAdapter)
    }

    override fun getAdapter(): Adapter<*>? {
        if (mWrapAdapter != null) return mWrapAdapter!!.innerAdapter
        return super.getAdapter()
    }

    /**
     * 设置 loadmore 判断时是：已划到顶部（SCROLL_DIRECTION_TOP）或者底部（SCROLL_DIRECTION_BOTTOM）
     * SCROLL_DIRECTION_TOP 适用于 IM 中历史消息的加载
     */
    fun canScrollDirection(@ScrollDirection direction: Int) {
        this.mScrollDirection = direction
    }

    fun setLoadMoreEnabled(enabled: Boolean) {
        this.mLoadMoreEnabled = enabled
    }

    fun setLoadMoreListener(listener: LoadMoreListener) {
        this.mLoadMoreListener = listener
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }

    private inner class WrapAdapter<T : ViewHolder>(val innerAdapter: Adapter<T>) :
        Adapter<T>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
            return innerAdapter.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: T, position: Int) {
            innerAdapter.onBindViewHolder(holder, position)
        }

        override fun getItemCount(): Int = innerAdapter.itemCount

        override fun getItemViewType(position: Int): Int = innerAdapter.getItemViewType(position)

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            innerAdapter.onAttachedToRecyclerView(recyclerView)
        }

        override fun onViewAttachedToWindow(holder: T) {
            super.onViewAttachedToWindow(holder)
            innerAdapter.onViewAttachedToWindow(holder)
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)
            innerAdapter.onDetachedFromRecyclerView(recyclerView)
        }

        override fun onViewDetachedFromWindow(holder: T) {
            super.onViewDetachedFromWindow(holder)
            innerAdapter.onViewDetachedFromWindow(holder)
        }

        override fun onViewRecycled(holder: T) {
            super.onViewRecycled(holder)
            innerAdapter.onViewRecycled(holder)
        }

        override fun onFailedToRecycleView(holder: T): Boolean {
            return innerAdapter.onFailedToRecycleView(holder)
        }

        override fun unregisterAdapterDataObserver(observer: AdapterDataObserver) {
            innerAdapter.unregisterAdapterDataObserver(observer)
        }

        override fun registerAdapterDataObserver(observer: AdapterDataObserver) {
            innerAdapter.registerAdapterDataObserver(observer)
        }
    }

    private inner class DataObserver : AdapterDataObserver() {
        override fun onChanged() {
            mWrapAdapter?.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mWrapAdapter?.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            mWrapAdapter?.notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mWrapAdapter?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mWrapAdapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            mWrapAdapter?.notifyItemRangeRemoved(fromPosition, toPosition)
        }
    }

}