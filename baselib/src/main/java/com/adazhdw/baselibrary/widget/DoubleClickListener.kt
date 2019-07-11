package com.adazhdw.baselibrary.widget

import android.view.MotionEvent
import android.view.View

abstract class DoubleClickListener : View.OnTouchListener {

    private var lastClickTime = 0L
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val currentTime = System.currentTimeMillis()
                if ((currentTime - lastClickTime) < 300) {
                    onDoubleClick()
                }
                lastClickTime = currentTime
            }
        }
        return false
    }

    abstract fun onDoubleClick()
}

fun View.setOnDoubleClickListener(listener: DoubleClickListener){
    setOnTouchListener(listener)
}