package com.adazhdw.ktlib.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.adazhdw.ktlib.base.IActivity
import org.greenrobot.eventbus.EventBus


/**
 * daguozhu
 * create at 2020/4/2 15:37
 * description:
 */

abstract class ViewBindingActivity : ForResultActivity(), IActivity {

    final override val layoutId: Int
        get() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isViewBinding()) {
            initBinding()
        } else {
            return
        }
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

    open fun isViewBinding(): Boolean = true
    abstract fun initBinding(): ViewDataBinding

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> {
        return lazy { DataBindingUtil.setContentView(this, resId) }
    }
}
