package com.adazhdw.ktlib.list.view

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView

class DataObserver(private val wrapAdapter: WrapperAdapter) : RecyclerView.AdapterDataObserver() {
    @SuppressLint("NotifyDataSetChanged")
    override fun onChanged() {
        wrapAdapter.notifyDataSetChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        wrapAdapter.notifyItemRangeChanged(positionStart, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        wrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        wrapAdapter.notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        wrapAdapter.notifyItemRangeRemoved(positionStart, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        wrapAdapter.notifyItemRangeRemoved(fromPosition, toPosition)
    }
}
