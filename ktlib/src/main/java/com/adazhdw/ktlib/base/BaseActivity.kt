package com.adazhdw.ktlib.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : ForResultActivity() {

    /**
     * 返回布局Id
     */
    protected abstract val layoutId: Int

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 初始化View
     */
    protected abstract fun initView()

    /**
     * 是否需要EventBus
     */
    protected open fun needEventBus(): Boolean = false

    /**
     * 网络请求开始
     */
    abstract fun requestStart()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
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

}

