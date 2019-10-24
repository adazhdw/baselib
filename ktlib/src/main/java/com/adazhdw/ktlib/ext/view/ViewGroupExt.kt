@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext.view

import android.view.View
import android.view.ViewGroup

/**
 * author: daguozhu
 * created on: 2019/10/24 16:26
 * description:
 */

/** get childView */
operator fun ViewGroup.get(index: Int) =
    getChildAt(index) ?: throw IndexOutOfBoundsException("Index: $index, childCount: $childCount")

/** return if [view] is found in this viewGroup */
inline operator fun ViewGroup.contains(view: View) = indexOfChild(view) != -1

/** add [view] to this viewGroup */
inline operator fun ViewGroup.plusAssign(view: View) = addView(view)

/** remove [view] from this viewGroup */
inline operator fun ViewGroup.minusAssign(view: View) = removeView(view)

/** Returns the number of views in this view group. */
inline val ViewGroup.size get() = childCount

/** Returns true if this view group contains no views. */
inline fun ViewGroup.isEmpty() = childCount == 0

/** Returns true if this view group contains one or more views. */
inline fun ViewGroup.isNotEmpty() = childCount != 0

/** Performs the given action on each view in this view group. */
inline fun ViewGroup.forEach(action: (view: View) -> Unit) {
    for (index in 0 until childCount) {
        action(getChildAt(index))
    }
}

/** Performs the given action on each view in this view group, providing its sequential index. */
inline fun ViewGroup.forEachIndexed(action: (index: Int, view: View) -> Unit) {
    for (index in 0 until childCount) {
        action(index, getChildAt(index))
    }
}

