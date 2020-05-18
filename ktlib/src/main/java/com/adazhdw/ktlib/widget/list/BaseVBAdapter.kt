package com.adazhdw.ktlib.widget.list

import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder


/**
 * Administrator
 * create at 2020/4/8 11:08
 * description: 使用 ViewBinding 的 RecyclerView 的 Adapter
 */
abstract class BaseVBAdapter<T> : BaseQuickAdapter<T, BaseVBViewHolder>(mutableListOf()) {
    protected val mHandler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVBViewHolder {
        if (mContext == null) mContext = parent.context
        return BaseVBViewHolder(viewBinding(parent, viewType))
    }

    override fun convert(helper: BaseVBViewHolder, item: T) {
        val position = helper.adapterPosition
        if (mData.isEmpty() || position == RecyclerView.NO_POSITION) return
        bindHolder(helper, item, position)
    }

    abstract fun bindHolder(holder: BaseVBViewHolder, data: T, position: Int)

    abstract fun viewBinding(parent: ViewGroup, viewType: Int): ViewBinding

    override fun getItemCount(): Int = mData.size

    fun clearData() {
        this.mData.clear()
        notifyDataSetChanged()
    }
}

class BaseVBViewHolder(val viewBinding: ViewBinding) : BaseViewHolder(viewBinding.root)