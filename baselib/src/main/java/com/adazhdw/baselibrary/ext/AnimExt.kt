package com.adazhdw.baselibrary.ext

import android.animation.ObjectAnimator
import android.view.View

/**
 * View 设置大小缩放
 * View需要事先设置scaleX和scaleY来适应即将产生的缩放变化
 */
fun View.viewSizeAnim(animDuration: Long = 300, scaleTo: Float = 1F) {
    /*val animScaleX = ObjectAnimator.ofFloat(this, "scaleX", scale, scaleTo)
    val animScaleY = ObjectAnimator.ofFloat(this, "scaleY", scale, scaleTo)
    AnimatorSet().apply {
        play(animScaleX).with(animScaleY)
        duration = animDuration
        start()
    }*/
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
    ObjectAnimator.ofFloat(this, "rotation", 0f, 360f).apply {
        duration = animDuration
        start()
    }
}

/**
 * View 旋转固定角度,默认360F
 */
fun View.rotation(rotation: Float = 360F, animDuration: Long = 300) {
    ObjectAnimator.ofFloat(this, "rotation", 0f, rotation).apply {
        duration = animDuration
        start()
    }
}

/**
 * View 透明度 0->1
 */
fun View.alphaVisible(animDuration: Long = 300) {
    val leftDuration: Long = ((1f - this.alpha) * animDuration).toLong()
    this.apply {
        animate()
            .apply { cancel() }
            .alpha(1f)
            .setDuration(leftDuration)
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
    val leftDuration: Long = ((1f - this.alpha) * animDuration).toLong()
    this.apply {
        animate()
            .apply { cancel() }
            .alpha(0F)
            .setDuration(leftDuration)
            .withEndAction {
                visibility = View.INVISIBLE
            }
            .start()
    }
}
