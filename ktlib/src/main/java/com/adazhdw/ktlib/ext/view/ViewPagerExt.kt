package com.adazhdw.ktlib.ext.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * author：adazhdw
 * date-time：2021/1/21 15:41
 * description：ViewPager 扩展方法
 **/

fun ViewPager.fragmentAdapter(fragments: List<Fragment>, fragmentManager: FragmentManager) {
    adapter = object : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment = fragments[position]
    }
}

fun ViewPager.fragmentStateAdapter(fragments: List<Fragment>, fragmentManager: FragmentManager) {
    adapter = object : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment = fragments[position]
    }
}
