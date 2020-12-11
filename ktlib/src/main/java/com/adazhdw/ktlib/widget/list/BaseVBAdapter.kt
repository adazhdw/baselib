package com.adazhdw.ktlib.widget.list

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


/**
 * daguozhu
 * create at 2020/4/8 11:08
 * description: 使用 ViewBinding 的 RecyclerView 的 Adapter
 */
abstract class BaseVBAdapter<T> : BaseAdapter<T, BaseVBViewHolder>() {

    override fun createHolder(parent: ViewGroup, viewType: Int): BaseVBViewHolder {
        return BaseVBViewHolder(viewBinding(parent, viewType))
    }

    abstract fun viewBinding(parent: ViewGroup, viewType: Int): ViewBinding
}
