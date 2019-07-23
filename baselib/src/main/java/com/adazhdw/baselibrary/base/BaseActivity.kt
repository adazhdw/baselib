package com.adazhdw.baselibrary.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : ForResultActivity() {

    protected val TAG = javaClass.simpleName + "------"
    protected val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    /**
     * 返回布局Id
     */
    abstract fun layoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 是否需要EventBus
     */
    open fun needEventBus(): Boolean = false

    /**
     * 网络请求开始
     */
    abstract fun requestStart()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initView()
        initData()
        requestStart()
        if (needEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

}

