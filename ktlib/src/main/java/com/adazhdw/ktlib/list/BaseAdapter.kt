package com.adazhdw.ktlib.list

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.ktlib.list.view.LoadMoreRecyclerView

/**
 * author：daguozhu
 * date-time：2020/12/15 9:26
 * description：继承自 RecyclerView.Adapter 抽象类
 **/
abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected var recyclerView: RecyclerView? = null
    private val layoutInflaterCache = SparseArray<LayoutInflater>()
    private var itemClickListener: ((holder: VH, data: T, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return notifyCreateVH(layoutInflaterCache.get(0) ?: LayoutInflater.from(parent.context), parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        val data = getItem(position)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(holder, data, position)
        }
        notifyBindVH(holder, data, position)
    }

    abstract fun notifyCreateVH(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH
    abstract fun notifyBindVH(holder: VH, data: T, position: Int)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflaterCache.put(0, LayoutInflater.from(recyclerView.context))
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        layoutInflaterCache.clear()
        this.recyclerView = null
    }

    fun setItemClickListener(listener: (holder: VH, data: T, position: Int) -> Unit) {
        this.itemClickListener = listener
    }

    override fun getItemCount(): Int = items.size

    private var items: MutableList<T> = mutableListOf()

    fun getData(): MutableList<T> = items

    fun getItem(position: Int): T = this.items[position]

    /**
     * 设置新的数据源
     */
    fun setNewData(data: MutableList<T>) {
        this.items = data
        notifyDataSetChanged()
    }

    /**
     * 设置新数据
     */
    fun setData(data: Collection<T>) {
        this.items.clear()
        this.items.addAll(data)
        notifyDataSetChanged()
    }

    /**
     * 设置某一条为新数据
     */
    fun setData(@IntRange(from = 0) index: Int, data: T) {
        if (index >= this.items.size) return
        this.items[index] = data
        notifyItemChanged(index)
    }

    /**
     * 添加一条新数据
     */
    fun addData(data: T) {
        this.items.add(data)
        notifyItemInserted(this.items.size)
    }

    /**
     * 添加列表数据
     */
    fun addData(data: Collection<T>) {
        this.items.addAll(data)
        notifyItemRangeInserted(this.items.size - data.size, data.size)
    }

    /**
     * 插入一条数据
     */
    fun addData(@IntRange(from = 0) index: Int, data: T) {
        if (index >= this.items.size) return
        this.items.add(index, data)
        notifyItemInserted(index)
    }

    /**
     * 插入列表数据
     */
    fun addData(@IntRange(from = 0) index: Int, data: Collection<T>) {
        if (index >= this.items.size) return
        this.items.addAll(index, data)
        notifyItemInserted(index)
    }

    /**
     * 移除指定位置的数据
     */
    fun removeAt(@IntRange(from = 0) index: Int) {
        if (index >= this.items.size) return
        this.items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeRemoved(index, this.items.size - index)
    }

    /**
     * 移除一条数据
     */
    fun remove(data: T) {
        val index = this.items.indexOf(data)
        if (index == -1) return
        removeAt(index)
    }

    /**
     * 清除数据
     */
    fun clearData() {
        this.items.clear()
        notifyDataSetChanged()
    }

    /**
     * 划到顶部
     */
    fun scrollToTop() {
        if (this.items.isNotEmpty()) {
            recyclerView?.scrollToPosition(0)
        }
    }

    /**
     * 划到底部
     */
    fun scrollToBottom() {
        if (this.items.isNotEmpty()) {
            recyclerView?.scrollToPosition(this.items.size - 1)
        }
    }

    /**
     * smooth划到顶部
     */
    fun smoothScrollToTop() {
        if (this.items.isNotEmpty()) {
            recyclerView?.smoothScrollToPosition(0)
        }
    }

    /**
     * smooth划到底部
     */
    fun smoothScrollToBottom() {
        if (this.items.isNotEmpty()) {
            recyclerView?.smoothScrollToPosition(this.items.size - 1)
        }
    }

    /**
     * 加载更多，加载成功
     */
    fun loadComplete(hasMore: Boolean, error: Boolean) {
        val recyclerView = this.recyclerView
        if (recyclerView is LoadMoreRecyclerView) {
            recyclerView.loadComplete(hasMore, error)
        }
    }

    /**
     * 加载更多，加载成功
     */
    fun loadEnd(gone: Boolean) {
        val recyclerView = this.recyclerView
        if (recyclerView is LoadMoreRecyclerView) {
            recyclerView.loadEnd(gone)
        }
    }

}