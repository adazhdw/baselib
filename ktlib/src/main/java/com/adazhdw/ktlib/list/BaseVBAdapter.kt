package com.adazhdw.ktlib.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


/**
 * daguozhu
 * create at 2020/4/8 11:08
 * description: 使用 ViewBinding 的 RecyclerView 的 Adapter
 */
abstract class BaseVBAdapter<T : Any> : BaseAdapter<T, BaseVBViewHolder>() {

    override fun notifyCreateVH(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseVBViewHolder {
        return BaseVBViewHolder(viewBinding(parent, inflater, viewType))
    }

    abstract fun viewBinding(parent: ViewGroup, inflater: LayoutInflater, viewType: Int): ViewBinding
}
