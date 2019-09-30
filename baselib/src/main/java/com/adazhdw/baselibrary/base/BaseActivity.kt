package com.adazhdw.baselibrary.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.LayoutRes
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : ForResultActivity() {

    protected val TAG = javaClass.simpleName + "------"
    protected val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

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

