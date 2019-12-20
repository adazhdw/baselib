package com.adazhdw.ktlib.list

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.ktlib.R
import kotlinx.android.synthetic.main.fragment_list_footer.view.*

abstract class ListAdapter(context: Context) : BaseAdapter(context), LoadMoreView {

    open val footerId: Int = R.layout.fragment_list_footer
    private var loadMoreStatus: Int = 0
    private var dataEmpty = false
    private var hasMore = true
    private var errorMsg: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(mLayoutInflater.inflate(viewType, parent, false))
    }

    final override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (!isLoadMoreView(position)) {
            bindHolder(holder, mData[position], position)
        } else {
            bindLoadMore(holder, position, loadMoreStatus)
        }
    }

    open fun bindLoadMore(holder: ListViewHolder, position: Int, loadMoreStatus: Int) {
        holder.itemView.loadTv.text = when (loadMoreStatus) {
            LoadMoreStatus.LOADING -> "加载中"
            LoadMoreStatus.LOAD_FINISH -> "没有更多数据了"
            LoadMoreStatus.LOAD_ERROR -> "加载出错 $errorMsg"
            else -> ""
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadMoreView(position)) footerId else layoutId
    }

    override fun getItemCount(): Int {
        return mData.size + if (loadMoreStatus != LoadMoreStatus.LOAD_FINISH) 1 else 0
    }

    /**
     * 判断是否是最后一个item
     */
    private fun isLoadMoreView(position: Int): Boolean = position == mData.size

    override fun loading() {
        loadMoreStatus = LoadMoreStatus.LOADING
        notifyItemChanged(itemCount)
    }

    override fun onLoadFinish(dataEmpty: Boolean, hasMore: Boolean) {
        this.dataEmpty = dataEmpty
        this.hasMore = hasMore
        if (!hasMore && dataEmpty) {
            loadMoreStatus = LoadMoreStatus.LOAD_FINISH
            notifyItemRemoved(itemCount)
        } else {
            loadMoreStatus = LoadMoreStatus.LOADING
            notifyItemChanged(itemCount)
        }
    }

    override fun onLoadError(errorCode: Int, errorMsg: String?) {
        this.errorMsg = errorMsg
        loadMoreStatus = LoadMoreStatus.LOAD_ERROR
        notifyItemChanged(itemCount)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        // 适配不同的LayoutManager
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isLoadMoreView(position)) manager.spanCount else 1
                }
            }
        }
    }

}