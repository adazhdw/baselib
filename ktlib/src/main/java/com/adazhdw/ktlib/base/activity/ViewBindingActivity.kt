package com.adazhdw.ktlib.base.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.adazhdw.ktlib.base.IActivity
import org.greenrobot.eventbus.EventBus


/**
 * daguozhu
 * create at 2020/4/2 15:37
 * description:
 */

abstract class ViewBindingActivity<T : ViewBinding> : ForResultActivity(), IActivity {

    final override val layoutId: Int
        get() = 0

    protected val viewBinding:T by lazy { initViewBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initViewBinding().root)
        window.setBackgroundDrawable(null)
        initView()
        initData()
        requestStart()
    }

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

    abstract fun initViewBinding(): T
}
