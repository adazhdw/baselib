package com.adazhdw.ktlib.list

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView


/**
 * author: daguozhu
 * created on: 2019/10/24 10:34
 * description:
 */

class BottomItemDecoration(@Px private val bottom: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, 0, 0, bottom)
        super.getItemOffsets(outRect, view, parent, state)
    }
}