package com.adazhdw.baselibrary.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.baselibrary.R

abstract class BaseRvAdapter<T>(private val mContext: Context?) : RecyclerView.Adapter<BaseViewHolder>() {

    var mData: MutableList<T> = mutableListOf()
        set(value) {
            val size = mData.size
            field.addAll(value)
            notifyItemRangeChanged(size, mData.size)
        }
    private var mInflater: LayoutInflater? = null
    var isRefresh: Boolean = false
    var isLoading: Boolean = false

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    abstract fun onLayoutId(): Int

    open fun onFooterLayoutId(): Int {
        return R.layout.list_base_footer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.context)
        }
        return if (viewType == onFooterLayoutId()) {
            BaseViewHolder(mInflater!!.inflate(onFooterLayoutId(), parent, false))
        } else {
            BaseViewHolder(mInflater!!.inflate(onLayoutId(), parent, false))
        }
    }

    final override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder.itemViewType != onFooterLayoutId()) {
            initData(holder, position)
        }
    }

    open fun initData(holder: BaseViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return if ((!isRefresh && isLoading) && (itemCount - 1) == position) {
            onFooterLayoutId()
        } else {
            super.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int = mData.size + (if (!isRefresh && isLoading) 1 else 0)

    fun showLoading() {
        notifyItemChanged(itemCount)
    }

}

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)