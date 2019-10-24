package com.adazhdw.ktlib.ext

import android.view.View

/**
 * View 设置大小缩放
 * View需要事先设置scaleX和scaleY来适应即将产生的缩放变化
 */
fun View.sizeAnim(animDuration: Long = 300, scaleTo: Float = 1.5F) {
    this.apply {
        animate()
            .apply { cancel() }
            .scaleX(scaleTo)
            .scaleY(scaleTo)
            .setDuration(animDuration)
            .start()
    }
}

/**
 * View 旋转360
 */
fun View.rotation360(animDuration: Long = 300) {
    this.animate()
        .apply { cancel() }
        .rotation(360F)
        .setDuration(animDuration)
        .start()
}

/**
 * View 旋转固定角度,默认360F
 */
fun View.rotation(rotation: Float = 360F, animDuration: Long = 300) {
    this.animate()
        .apply { cancel() }
        .rotation(rotation)
        .setDuration(animDuration)
        .start()
}

/**
 * View 透明度 0->1
 */
fun View.alphaVisible(animDuration: Long = 300) {
    this.apply {
        animate()
            .apply { cancel() }
            .alpha(1f)
            .setDuration(animDuration)
            .withEndAction {
                visibility = View.VISIBLE
            }
            .start()
    }
}

/**
 * View 透明度 1->0
 */
fun View.alphaGone(animDuration: Long = 300) {
    this.apply {
        animate()
            .apply { cancel() }
            .alpha(0F)
            .setDuration(animDuration)
            .withEndAction {
                visibility = View.INVISIBLE
            }
            .start()
    }
}
