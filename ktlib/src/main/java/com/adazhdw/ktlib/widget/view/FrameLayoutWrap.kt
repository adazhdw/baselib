package com.adazhdw.ktlib.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * 使用NestedScrollView测量子View的方法，自定义的一个不限制子View高度的Layout
 */
class FrameLayoutWrap : FrameLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun measureChild(child: View, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
        val lp = child.layoutParams

        val childWidthMeasureSpec: Int
        val childHeightMeasureSpec: Int = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        childWidthMeasureSpec =
            ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, paddingLeft + paddingRight, lp.width)

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    override fun measureChildWithMargins(
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ) {
        val lp = child.layoutParams as ViewGroup.MarginLayoutParams

        val childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(
            parentWidthMeasureSpec,
            paddingLeft + paddingRight + lp.leftMargin + lp.rightMargin
                    + widthUsed, lp.width
        )
        val childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            lp.topMargin + lp.bottomMargin,
            View.MeasureSpec.UNSPECIFIED
        )//Layout不限制子View的高度的核心代码

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        //        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

}
