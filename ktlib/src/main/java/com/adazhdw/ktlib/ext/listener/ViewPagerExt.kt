package com.adazhdw.ktlib.ext.listener

import androidx.viewpager.widget.ViewPager

/**
 * author: daguozhu
 * created on: 2019/10/22 19:10
 * description:
 */

fun ViewPager.onPageSelected(
    action: (position: Int) -> Unit
) = addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    override fun onPageSelected(position: Int) {

    }
})

fun ViewPager.onPageChange(
    onPageSelected: (pos: Int) -> Unit,
    onPageScrolled: ((pos: Int, posOffset: Float, posOffsetPixels: Int) -> Unit)? = null,
    onPageScrollStateChanged: ((state: Int) -> Unit)? = null
) = addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    override fun onPageSelected(position: Int) {

    }
})


fun ViewPager.setPageLoadMoreListener(listener: (() -> Unit)) {
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        private var hasScrolled = false
        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                ViewPager.SCROLL_STATE_DRAGGING -> {//每次滑动都会调用
                    hasScrolled = false
                }
                ViewPager.SCROLL_STATE_SETTLING -> {//除了最后一个Item，其他item滑动完成都会调用。
                    hasScrolled = true
                }
                ViewPager.SCROLL_STATE_IDLE -> {//每次滑动完成都会调用。
                    if (!hasScrolled && currentItem == ((adapter?.count ?: 0) - 1)) {
                        listener.invoke()
                    }
                    hasScrolled = true
                }
            }
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {

        }
    })
}