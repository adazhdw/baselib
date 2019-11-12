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

inline val FrameLayout.layoutParamsW_W
    get() = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
    )

inline val FrameLayout.layoutParamsM_M
    get() = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
    )

inline val FrameLayout.layoutParamsM_W
    get() = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
    )

inline val ConstraintLayout.layoutParamsW_W
    get() = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
    )

inline val ConstraintLayout.layoutParamsM_M
    get() = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    )

inline val ConstraintLayout.layoutParamsM_W
    get() = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
    )

inline val LinearLayout.layoutParamsW_W
    get() = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    )

inline val LinearLayout.layoutParamsM_M
    get() = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
    )

inline val LinearLayout.layoutParamsM_W
    get() = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    )

inline val RelativeLayout.layoutParamsW_W
    get() = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    )

inline val RelativeLayout.layoutParamsM_M
    get() = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
    )

inline val RelativeLayout.layoutParamsM_W
    get() = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    )