package com.adazhdw.ktlib.ext.recycler

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


/**
 * author: daguozhu
 * created on: 2019/10/21 16:07
 * description: RecyclerView实现拖拽移动位置
 */

class ItemTouchCallback(private val mTouchAdapter: ItemTouchAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (mTouchAdapter.onIllegalPosition(viewHolder.layoutPosition)) return 0
        return if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            //final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            val swipeFlags = 0
            makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition//得到拖动ViewHolder的position
        val toPosition = target.adapterPosition//得到目标ViewHolder的position
        mTouchAdapter.onMove(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        mTouchAdapter.onSwiped(position)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            mTouchAdapter.onSelected(viewHolder, true)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        mTouchAdapter.onSelected(viewHolder,false)
    }


    interface ItemTouchAdapter {
        fun onMove(fromPosition: Int, toPosition: Int)

        fun onSwiped(position: Int)

        fun onSelected(viewHolder: RecyclerView.ViewHolder?, b: Boolean)

        fun onIllegalPosition(position: Int): Boolean
    }
}