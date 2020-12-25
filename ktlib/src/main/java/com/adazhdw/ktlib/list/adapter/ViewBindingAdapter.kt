package com.adazhdw.ktlib.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.adazhdw.ktlib.list.holder.BaseVBViewHolder


/**
 * daguozhu
 * create at 2020/4/8 11:08
 * description: 使用 ViewBinding 的 RecyclerView 的 Adapter
 */
abstract class ViewBindingAdapter<T : Any> : ListAdapter<T, BaseVBViewHolder>() {

    override fun notifyCreateHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseVBViewHolder {
        return BaseVBViewHolder(viewBinding(parent, inflater, viewType))
    }

    abstract fun viewBinding(parent: ViewGroup, inflater: LayoutInflater, viewType: Int): ViewBinding
}
