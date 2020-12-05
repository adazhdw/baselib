package com.adazhdw.ktlib.widget.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * daguozhu
 * create at 2020/4/8 19:53
 * description:
 */
class LinearLayoutManagerWrapper : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}