package com.adazhdw.baselibrary.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View

/**
 * View 由无到有显示
 */
fun View.animateVisible(animDuration: Long = 300) {
    this.let { view ->
//        alpha = 0F//不设置alpha，让view从自己现在的alpha开始变化
        visibility = View.VISIBLE
        animate()
            .alpha(1F)
            .setDuration(animDuration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.VISIBLE //优化step
                }
            })
    }
}

fun View.animateViewVisible(time: Long = 300) {
    ObjectAnimator.ofFloat(this.apply {
//        alpha = 0F
        visibility = View.VISIBLE
    }, "alpha", 0F, 1F).apply {
        duration = time
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                this@animateViewVisible.visibility = View.VISIBLE //优化step
            }
        })
        start()
    }
}

/**
 * View 隐藏
 */
fun View.animateGone(animDuration: Long = 300) {
    this.let { view ->
//        alpha = 1F
        view.animate()
            .setDuration(animDuration)
            .alpha(0F)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.INVISIBLE //优化step
                }
            })
    }
}

fun View.animateViewGone(time: Long = 300) {
    ObjectAnimator.ofFloat(this, "alpha",  0F).apply {
        duration = time
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                this@animateViewGone.visibility = View.INVISIBLE //优化step
            }
        })
        start()
    }
}

/**
 * View 向下移动
 */
fun View.animateYDown(animDuration: Long = 300, distance: Float = 50F, endAction: (() -> Unit)? = null) {
    /*ObjectAnimator.ofFloat(this, "translationY", distance).apply {
        duration = animDuration
        start()
    }*/
    this.apply {
        animate()
            .apply { cancel() }
            .translationY(distance)
            .setDuration(animDuration)
            .withEndAction {
                endAction?.invoke()
            }.start()
    }
}

/**
 * View 向上移动
 */
fun View.animateYUp(animDuration: Long = 300, distance: Float = 50F, endAction: (() -> Unit)? = null) {
    /*ObjectAnimator.ofFloat(this, "translationY", -distance).apply {
        duration = animDuration
        start()
    }*/
    this.apply {
        animate()
            .apply { cancel() }
            .translationY(-distance)
            .setDuration(animDuration)
            .withEndAction {
                endAction?.invoke()
            }.start()
    }
}

/**
 * View 向右移动
 */
fun View.animateXToRight(animDuration: Long = 300, distance: Float = 50F, endAction: (() -> Unit)? = null) {
    /*ObjectAnimator.ofFloat(this, "translationX", distance).apply {
        duration = animDuration
        start()
    }*/
    this.apply {
        animate()
            .apply { cancel() }
            .translationX(distance)
            .setDuration(animDuration)
            .withEndAction {
                endAction?.invoke()
            }.start()
    }
}

/**
 * View 向左移动
 */
fun View.animateXToLeft(animDuration: Long = 300, distance: Float = 50F, endAction: (() -> Unit)? = null) {
    /*ObjectAnimator.ofFloat(this, "translationX", -distance).apply {
        duration = animDuration
        start()
    }*/
    this.apply {
        animate()
            .apply { cancel() }
            .translationX(-distance)
            .setDuration(animDuration)
            .withEndAction {
                endAction?.invoke()
            }.start()
    }
}

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
