package com.adazhdw.ktlib.list.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * author：daguozhu
 * date-time：2020/12/15 9:26
 * description：继承自 RecyclerView.Adapter 抽象类
 **/
abstract class BaseAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected var recyclerView: RecyclerView? = null
    private val layoutInflaterCache = SparseArray<LayoutInflater>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return notifyCreateHolder(layoutInflaterCache.get(0) ?: LayoutInflater.from(parent.context), parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        notifyBind(holder, position)
    }

    abstract fun notifyCreateHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH
    abstract fun notifyBind(holder: VH, position: Int)

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

}