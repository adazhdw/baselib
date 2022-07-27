package com.adazhdw.ktlib.list.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.annotation.NonNull
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
class LoadMoreRecyclerView : RecyclerView {

    companion object {
        const val SCROLL_DIRECTION_TOP = -1
        const val SCROLL_DIRECTION_BOTTOM = 1

        private const val VIEW_TYPE_HEAD = 20000001
        private const val VIEW_TYPE_FOOTER = 20000002
        private const val VIEW_TYPE_LOAD_MORE = 20000003
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    @IntDef(SCROLL_DIRECTION_TOP, SCROLL_DIRECTION_BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class ScrollDirection {}

    private var isLoading = false//是否正在进行网络请求
    private var hasMoreData = true// 是否有更多数据
    private var isLoadMoreAvailable = false//总开关，控制loadMore是否可用
    private var isLoadMoreEnabled = false
    private var mLoadMoreListener: LoadMoreListener? = null
    private var mWrapAdapter: WrapAdapter? = null
    private var mDataObserver: DataObserver? = null
    private var mScrollDirection: Int = SCROLL_DIRECTION_BOTTOM
    private lateinit var loadMoreView: LoadMoreView

    private fun initView(context: Context) {
        loadMoreView = LoadMoreViewImpl(context)
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
        val itemCount = layoutManager?.itemCount ?: 0
        if (itemCount <= 0) return
        val lastVisiblePosition: Int
        var canLoadMore = false
        when (layoutManager) {
            is GridLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                //if (itemCount == lastVisiblePosition + 1) canLoadMore = true
                if (itemCount == lastVisiblePosition) canLoadMore = true//因为增加了loadView，所以计算需要-1
            }
            is StaggeredGridLayoutManager -> {
                val into = intArrayOf(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(into)
                lastVisiblePosition = into.maxOrNull() ?: 0
                //if (itemCount == lastVisiblePosition + 1) canLoadMore = true
                if (itemCount == lastVisiblePosition) canLoadMore = true//因为增加了loadView，所以计算需要-1
            }
            is LinearLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                //if (itemCount == lastVisiblePosition + 1) canLoadMore = true
                if (itemCount == lastVisiblePosition) canLoadMore = true//因为增加了loadView，所以计算需要-1
            }
        }
        if (isLoadMoreAvailable && canLoadMore && state == SCROLL_STATE_IDLE && alreadyTopOrBottom() && isLoadMoreEnabled && hasMoreData && !isLoading) {
            doLoadMore()
        }
    }

    private fun alreadyTopOrBottom() = !canScrollVertically(mScrollDirection)

    private fun doLoadMore() {
        this.isLoading = true
        this.hasMoreData = true
        this.loadMoreView.loading()
        this.mLoadMoreListener?.onLoadMore()
    }

    fun loadComplete(hasMore: Boolean, error: Boolean = false) {
        this.isLoading = false
        this.hasMoreData = hasMore
        if (error) {
            this.loadMoreView.loadError()
        } else {
            this.loadMoreView.loadSuccess()
        }
    }

    /**
     * 设置 loadmore 判断时是：已划到顶部（SCROLL_DIRECTION_TOP）或者底部（SCROLL_DIRECTION_BOTTOM）
     * SCROLL_DIRECTION_TOP 适用于 IM 中历史消息的加载
     */
    fun canScrollDirection(@ScrollDirection direction: Int) {
        this.mScrollDirection = direction
    }

    fun setLoadMoreEnabled(enabled: Boolean) {
        this.isLoadMoreEnabled = enabled
    }

    fun setLoadMoreAvailable(available: Boolean) {
        this.isLoadMoreAvailable = available
    }

    fun setLoadMoreListener(listener: LoadMoreListener) {
        this.mLoadMoreListener = listener
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }

    fun setLoadMoreView(@NonNull loadMoreView: LoadMoreView) {
        this.loadMoreView = loadMoreView
    }

    //------------------------------------------------
    //------ Adapter 相关
    //------------------------------------------------

    @Suppress("UNCHECKED_CAST")
    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter == null) return
        mWrapAdapter = WrapAdapter(adapter as Adapter<ViewHolder>)
        if (mDataObserver != null) {
            // 为原有的RecyclerAdapter移除数据监听对象
            adapter.unregisterAdapterDataObserver(mDataObserver!!)
        }
        if (mDataObserver == null) {
            mDataObserver = DataObserver()
            // 为原有的RecyclerAdapter添加数据监听对象
            adapter.registerAdapterDataObserver(mDataObserver!!)
            mDataObserver?.onChanged()
        }
        super.setAdapter(mWrapAdapter)
    }

    override fun getAdapter(): Adapter<*>? {
        if (mWrapAdapter != null) return mWrapAdapter!!.innerAdapter
        return super.getAdapter()
    }

    private inner class WrapAdapter(val innerAdapter: Adapter<ViewHolder>) : Adapter<ViewHolder>() {

        private inner class WrapViewHolder(itemView: View) : ViewHolder(itemView)

        fun innerItemCount() = innerAdapter.itemCount

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            if (viewType == VIEW_TYPE_LOAD_MORE) {
                return WrapViewHolder(loadMoreView.getContentView())
            }
            return innerAdapter.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (position == NO_POSITION) return
            if (isLoadMorePos(position)) return
            innerAdapter.onBindViewHolder(holder, position)
        }

        private fun isLoadMorePos(position: Int): Boolean {
            return isLoadMoreEnabled && isLoadMoreAvailable && position == (itemCount - 1)
        }

        override fun getItemCount(): Int {
            return if (isLoadMoreEnabled && isLoadMoreAvailable) {
                innerItemCount() + 1/*if (hasMoreData) 1 else 0*/
            } else innerItemCount()
        }

        override fun getItemViewType(position: Int): Int {
            if (isLoadMorePos(position)) return VIEW_TYPE_LOAD_MORE
            return innerAdapter.getItemViewType(position)
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            val layoutManager = recyclerView.layoutManager
            if (layoutManager is GridLayoutManager) {
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (isLoadMorePos(position)) layoutManager.spanCount else 1
                    }
                }
            }
            innerAdapter.onAttachedToRecyclerView(recyclerView)
        }

        override fun onViewAttachedToWindow(holder: ViewHolder) {
            super.onViewAttachedToWindow(holder)
            val lp = holder.itemView.layoutParams
            val position = holder.layoutPosition
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                if (isLoadMorePos(position)) lp.isFullSpan = true
            }
            innerAdapter.onViewAttachedToWindow(holder)
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)
            innerAdapter.onDetachedFromRecyclerView(recyclerView)
        }

        override fun onViewDetachedFromWindow(holder: ViewHolder) {
            super.onViewDetachedFromWindow(holder)
            innerAdapter.onViewDetachedFromWindow(holder)
        }

        override fun onViewRecycled(holder: ViewHolder) {
            super.onViewRecycled(holder)
            innerAdapter.onViewRecycled(holder)
        }

        override fun onFailedToRecycleView(holder: ViewHolder): Boolean {
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