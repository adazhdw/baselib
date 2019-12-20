package com.adazhdw.ktlib.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter(private val mContext:Context) : RecyclerView.Adapter<ListViewHolder>() {

    protected val mLayoutInflater: LayoutInflater by lazy { LayoutInflater.from(mContext) }
    protected val mData: MutableList<Any> = mutableListOf()
    abstract val layoutId: Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(mLayoutInflater.inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        bindHolder(holder, mData[position], position)
    }

    abstract fun bindHolder(holder: ListViewHolder, data: Any, position: Int)

    override fun getItemCount(): Int = mData.size

    fun addData(data: Any) {
        val size = this.mData.size
        this.mData.add(data)
        notifyItemRangeChanged(size, this.mData.size)
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

    fun data(): List<Any> {
        return mData
    }

}