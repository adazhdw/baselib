package com.adazhdw.ktlib.base.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.adazhdw.ktlib.base.IActivity
import org.greenrobot.eventbus.EventBus


/**
 * Administrator
 * create at 2020/4/2 15:37
 * description:
 */

abstract class BaseActivityBinding : ForResultActivity(), IActivity {

    final override val layoutId: Int
        get() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isViewBinding()) {
            val view = initViewBinding().root
            setContentView(view)
        } else {
            return
        }
        window.setBackgroundDrawable(null)
        initView()
        initData()
        requestStart()
    }

    abstract fun initViewBinding(): ViewBinding

    override fun onStart() {
        super.onStart()
        if (needEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        super.onStop()
        if (needEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    open fun isViewBinding(): Boolean = true

}
