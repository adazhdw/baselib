package com.adazhdw.ktlib.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * author：daguozhu
 * date-time：2020/12/15 9:26
 * description：继承自 RecyclerView.Adapter 抽象类
 **/
abstract class AbsAdapter<VH : RecyclerView.ViewHolder>(context: Context) : RecyclerView.Adapter<VH>() {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected var recyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return notifyCreateHolder(inflater, parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        notifyBind(holder, position)
    }

    abstract fun notifyCreateHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH
    abstract fun notifyBind(holder: VH, position: Int)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

}