package com.adazhdw.ktlib.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {

    lateinit var mContext: Context
    private val mData: MutableList<Any> = mutableListOf()
    private var isLoading = false
    private val mLayoutInflater by lazy { LayoutInflater.from(mContext) }
    private var mLoadMoreView: ListRecyclerView.LoadMoreView? = null
    private var addDataEmpty = false

    @LayoutRes
    abstract fun layoutId(): Int

    private fun footerId(): Int = 1024

    abstract fun bindHolder(holder: ListViewHolder, data: Any, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        if (viewType == footerId() && mLoadMoreView != null && mLoadMoreView is View)
            return ListViewHolder(mLoadMoreView as View)
        return ListViewHolder(mLayoutInflater.inflate(viewType, parent, false))
    }

    override fun getItemCount(): Int = mData.size + if (isLoading && !addDataEmpty) 1 else 0

    final override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (!isLastItem(position) && !isLoading) {
            bindHolder(holder, mData[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLastItem(position)) {
            footerId()
        } else {
            layoutId()
        }
    }

    /**
     * 判断是否是最后一个item
     */
    private fun isLastItem(position: Int): Boolean {
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
        addDataEmpty = list.isEmpty()
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
        notifyItemChanged(itemCount)
    }

    fun setLoadMoreView(loadMoreView: ListRecyclerView.LoadMoreView) {
        this.mLoadMoreView = loadMoreView
    }

}