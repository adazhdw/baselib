package com.adazhdw.ktlib.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {

    lateinit var mContext: Context
    private val footerId :Int = 1024
    private val mData: MutableList<Any> = mutableListOf()
    private var isLoading = false
    private val mLayoutInflater by lazy { LayoutInflater.from(mContext) }
    private var mLoadMoreView: LoadMoreView? = null

    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun bindHolder(holder: ListViewHolder, data: Any, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        if (viewType == footerId && mLoadMoreView != null && mLoadMoreView is View)
            return ListViewHolder(mLoadMoreView as View)
        return ListViewHolder(mLayoutInflater.inflate(layoutId(), parent, false))
    }

    final override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (!isLoadMoreView(position) && !isLoading) {
            bindHolder(holder, mData[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadMoreView(position)) {
            footerId
        } else {
            super.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int {
        return mData.size + if (isLoading && mLoadMoreView != null) 1 else 0
    }

    /**
     * 判断是否是最后一个item
     */
    private fun isLoadMoreView(position: Int): Boolean {
        return position == mData.size
    }

    fun setData(list: List<Any>) {
        this.mData.clear()
        this.mData.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<Any>) {
        if (list.isNotEmpty()) {
            val size = this.mData.size
            this.mData.addAll(list)
            notifyItemRangeChanged(size, this.mData.size)
        }
    }

    fun clearData() {
        this.mData.clear()
        notifyDataSetChanged()
    }

    fun getData(): List<Any> {
        return mData
    }

    fun loading(loading: Boolean) {
        isLoading = loading
        if (isLoading) {
            notifyItemChanged(itemCount)
        } else {
            notifyItemRemoved(itemCount)
        }
    }

    fun setLoadMoreView(loadMoreView: LoadMoreView) {
        this.mLoadMoreView = loadMoreView
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