package com.adazhdw.baselibrary.utils

import android.view.View

object AnimUtils {

    private const val TAG = "AnimUtils"
    private const val animDuration: Long = 300

    fun viewSizeAnim(view: View, time: Long = animDuration, scaleTo: Float = 1F) {
        view.animate()
            .apply { cancel() }
            .scaleX(scaleTo)
            .scaleY(scaleTo)
            .setDuration(time)
            .start()

    }

    fun moveY(view: View, height: Float = 50F, time: Long = animDuration) {
        view.animate()
            .apply { cancel() }
            .translationY(height)
            .setDuration(time)
            .withEndAction {

            }.start()
    }

    fun animateViewVisible(view: View, time: Long = animDuration) {
        val leftDuration: Long = ((1f - view.alpha) * time).toLong()
        view.animate()
            .apply { cancel() }
            .alpha(1f)
            .setDuration(leftDuration)
            .withEndAction {

            }
            .start()
    }

    fun animateViewGone(view: View, time: Long = animDuration) {
        val leftDuration: Long = ((1f - view.alpha) * time).toLong()
        view.animate()
            .apply { cancel() }
            .alpha(0F)
            .setDuration(leftDuration)
            .withEndAction {

            }
            .start()
    }

    fun animViewVisible_X(view: View, time: Long = animDuration, height: Float = 50F) {
        val leftDuration: Long = ((1f - view.alpha) * time).toLong()
        view.animate()
            .apply { cancel() }
            .alpha(1f)
            .setDuration(leftDuration)
            .translationX(height)
            .withEndAction {

            }
            .start()
    }

    fun animViewGone_Y(view: View, time: Long = animDuration, height: Float = 50F) {
        val leftDuration: Long = ((1f - view.alpha) * time).toLong()
        view.animate()
            .apply { cancel() }
            .alpha(0F)
            .setDuration(leftDuration)
            .translationY(height)
            .withEndAction {

            }
            .start()
    }

    /**
     * View 旋转360
     */
    fun rotation360(view: View, animDuration: Long = 300) {
        /*ObjectAnimator.ofFloat(view, "rotation", 0f, 360f).apply {
            duration = animDuration
            start()
        }*/
        view.animate()
            .rotation(360F)
            .setDuration(animDuration)
            .start()
    }

    /**
     * View 旋转固定角度,默认360F
     */
    fun rotation(view: View, rotation: Float = 360F, animDuration: Long = 300) {
        /*ObjectAnimator.ofFloat(view, "rotation", 0f, rotation).apply {
            duration = animDuration
            start()
        }*/
        view.animate()
            .rotation(rotation)
            .setDuration(animDuration)
            .start()
    }

    /**
     * View 透明度 0->1
     */
    fun alphaVisible(view: View, animDuration: Long = 300) {
        /*ObjectAnimator.ofFloat(view, "alpha", 0F, 1F).apply {
            duration = animDuration
            start()
        }*/
        view.animate()
            .alpha(1F)
            .setDuration(animDuration)
            .withEndAction {
                view.visibility = View.VISIBLE
            }
            .start()
    }

    /**
     * View 透明度 1->0
     */
    fun alphaGone(view: View, animDuration: Long = 300) {
        /*ObjectAnimator.ofFloat(view, "alpha", 1F, 0F).apply {
            duration = animDuration
            start()
        }*/
        view.animate()
            .alpha(0F)
            .setDuration(animDuration)
            .withEndAction {
                view.visibility = View.INVISIBLE
            }
            .start()
    }
}