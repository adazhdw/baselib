package com.adazhdw.ktlib.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.ktlib.list.holder.BaseViewHolder

/**
 * author：daguozhu
 * date-time：2020/12/10 17:32
 * description：
 **/
abstract class BaseAdapter<T, VH : BaseViewHolder> : RecyclerView.Adapter<VH>() {

    protected var mData: MutableList<T> = mutableListOf()
    protected var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        if (inflater == null) inflater = LayoutInflater.from(parent.context)
        return createHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        bindHolder(holder, mData[position], position)
    }

    override fun getItemCount(): Int = mData.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (inflater == null) inflater = LayoutInflater.from(recyclerView.context)
    }

    abstract fun createHolder(parent: ViewGroup, viewType: Int): VH
    abstract fun bindHolder(holder: VH, data: T, position: Int)

    fun getData(): MutableList<T> = mData

    /**
     * 设置新的数据源
     */
    fun setNewData(data: MutableList<T>) {
        this.mData = data
        notifyDataSetChanged()
    }

    /**
     * 设置新数据
     */
    fun setData(data: Collection<T>) {
        this.mData.clear()
        this.mData.addAll(data)
        notifyDataSetChanged()
    }

    /**
     * 设置某一条为新数据
     */
    fun setData(@IntRange(from = 0) index: Int, data: T) {
        if (index >= this.mData.size) return
        this.mData[index] = data
        notifyItemChanged(index)
    }

    /**
     * 添加一条新数据
     */
    fun addData(data: T) {
        this.mData.add(data)
        notifyItemInserted(this.mData.size)
    }

    /**
     * 添加列表数据
     */
    fun addData(data: Collection<T>) {
        this.mData.addAll(data)
        notifyItemRangeInserted(this.mData.size - data.size, data.size)
    }

    /**
     * 插入一条数据
     */
    fun addData(@IntRange(from = 0) index: Int, data: T) {
        if (index >= this.mData.size) return
        this.mData.add(index, data)
        notifyItemInserted(index)
    }

    /**
     * 插入列表数据
     */
    fun addData(@IntRange(from = 0) index: Int, data: Collection<T>) {
        if (index >= this.mData.size) return
        this.mData.addAll(index, data)
        notifyItemInserted(index)
    }

    /**
     * 移除指定位置的数据
     */
    fun removeAt(@IntRange(from = 0) index: Int) {
        if (index >= this.mData.size) return
        this.mData.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeRemoved(index, this.mData.size - index)
    }

    /**
     * 移除一条数据
     */
    fun remove(data: T) {
        val index = this.mData.indexOf(data)
        if (index == -1) return
        removeAt(index)
    }

    /**
     * 清除数据
     */
    fun clearData() {
        this.mData.clear()
        notifyDataSetChanged()
    }
}