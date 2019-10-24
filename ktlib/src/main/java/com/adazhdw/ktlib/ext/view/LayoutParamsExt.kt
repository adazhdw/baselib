@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.adazhdw.ktlib.ext.view

import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * author: daguozhu
 * created on: 2019/10/24 13:41
 * description:
 */

inline fun FrameLayout.layoutParamsWrap() = FrameLayout.LayoutParams(
    FrameLayout.LayoutParams.WRAP_CONTENT,
    FrameLayout.LayoutParams.WRAP_CONTENT
)

inline fun FrameLayout.layoutParamsMatch() = FrameLayout.LayoutParams(
    FrameLayout.LayoutParams.MATCH_PARENT,
    FrameLayout.LayoutParams.MATCH_PARENT
)

inline fun ConstraintLayout.layoutParamsWrap() = FrameLayout.LayoutParams(
    ConstraintLayout.LayoutParams.WRAP_CONTENT,
    ConstraintLayout.LayoutParams.WRAP_CONTENT
)

inline fun ConstraintLayout.layoutParamsMatch() = FrameLayout.LayoutParams(
    ConstraintLayout.LayoutParams.MATCH_PARENT,
    ConstraintLayout.LayoutParams.MATCH_PARENT
)

inline fun LinearLayout.layoutParamsWrap() = FrameLayout.LayoutParams(
    LinearLayout.LayoutParams.WRAP_CONTENT,
    LinearLayout.LayoutParams.WRAP_CONTENT
)

inline fun LinearLayout.layoutParamsMatch() = FrameLayout.LayoutParams(
    LinearLayout.LayoutParams.MATCH_PARENT,
    LinearLayout.LayoutParams.MATCH_PARENT
)

inline fun RelativeLayout.layoutParamsWrap() = FrameLayout.LayoutParams(
    RelativeLayout.LayoutParams.WRAP_CONTENT,
    RelativeLayout.LayoutParams.WRAP_CONTENT
)

inline fun RelativeLayout.layoutParamsMatch() = FrameLayout.LayoutParams(
    RelativeLayout.LayoutParams.MATCH_PARENT,
    RelativeLayout.LayoutParams.MATCH_PARENT
)