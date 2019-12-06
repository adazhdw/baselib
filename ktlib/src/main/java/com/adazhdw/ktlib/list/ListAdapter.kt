package com.adazhdw.ktlib.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.ktlib.R

abstract class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {

    lateinit var mContext: Context
    private val mData: MutableList<Any> = mutableListOf()
    private var isLoading = false
    private val mLayoutInflater by lazy { LayoutInflater.from(mContext) }
    private var mLoadMoreView: ListRecyclerView.LoadMoreView? = null

    @LayoutRes
    abstract fun layoutId(): Int

    private fun footerId(): Int = 1024

    abstract fun bindHolder(holder: ListViewHolder, data: Any, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        if (viewType == footerId() && mLoadMoreView != null && mLoadMoreView is View)
            return ListViewHolder(mLoadMoreView as View)
        return ListViewHolder(mLayoutInflater.inflate(viewType, parent, false))
    }

    override fun getItemCount(): Int = mData.size + if (isLoading) 1 else 0

    final override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (!isLastItem(position)) {
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
    protected fun isLastItem(position: Int): Boolean {
        return position == mData.size
    }

    fun setData(list: List<Any>) {
        this.mData.clear()
        this.mData.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<Any>) {
        val size = this.mData.size
        this.mData.addAll(list)
        notifyItemRangeChanged(size, this.mData.size)
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