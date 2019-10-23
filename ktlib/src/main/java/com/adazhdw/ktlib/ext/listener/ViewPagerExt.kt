package com.adazhdw.ktlib.ext.listener

import androidx.viewpager.widget.ViewPager

/**
 * author: daguozhu
 * created on: 2019/10/22 19:10
 * description:
 */

fun ViewPager.onPageSelected(action: (position: Int) -> Unit) =
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
) =
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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