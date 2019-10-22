package com.adazhdw.ktlib.utils

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardUtil {

    /**
     * 动态隐藏软键盘
     * @param activity
     */
    fun hideSoftInput(activity: Activity) {
        val view = activity.window.peekDecorView()
        if (view != null) {
            val inputmanger = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmanger.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * 动态隐藏软键盘
     * @param context
     * @param edit
     */
    fun hideSoftInput(context: Context, edit: EditText) {
        edit.clearFocus()

        val inputmanger = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputmanger.hideSoftInputFromWindow(edit.windowToken, 0)
    }

    /**
     * 动态显示软键盘
     * @param context
     * @param edit
     */
    fun showSoftInput(context: Context, edit: EditText) {
        edit.isFocusable = true
        edit.isFocusableInTouchMode = true
        edit.requestFocus()

        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(edit, 0)
    }


    /**
     * 是否落在 EditText 区域
     */
    fun isHideKeyboard(view: View?, event: MotionEvent): Boolean {
        if (view != null && view is EditText) {
            val location = intArrayOf(0, 0)
            view.getLocationInWindow(location)
            //获取现在拥有焦点的控件view的位置，即EditText
            val left = location[0]
            val top = location[1]
            val bottom = top + view.height
            val right = left + view.width
            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            val isInEt = (event.x > left && event.x < right && event.y > top && event.y < bottom)
            return !isInEt
        }
        return false
    }
}
