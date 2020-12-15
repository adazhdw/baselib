package com.adazhdw.ktlib.list.adapter

import android.content.Context
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView

/**
 * author：daguozhu
 * date-time：2020/12/15 15:18
 * description：继承自 AbsAdapter 的 数据管理类
 **/
abstract class ListAdapter<T : Any, VH : RecyclerView.ViewHolder>(context: Context) : AbsAdapter<VH>(context) {

    private var items: MutableList<T> = mutableListOf()

    override fun getItemCount(): Int = items.size

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

}