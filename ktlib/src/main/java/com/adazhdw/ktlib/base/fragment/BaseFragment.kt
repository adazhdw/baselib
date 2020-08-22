package com.adazhdw.ktlib.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adazhdw.ktlib.base.IFragment
import org.greenrobot.eventbus.EventBus

/**
 * 1、布局初始化后，才能加载数据
 * 2、界面是否可见后，才能加载数据
 * 3、是否加载过数据，如果加载过，就不重复加载
 */
abstract class BaseFragment : CoroutinesFragment(),
    IFragment {

    /**
     * 是否初始化过布局
     */
    protected var isViewInitiated = false

    /**
     * 是否加载过数据
     */
    protected var isDataInitiated = false

    protected var mContentView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initData()
        mContentView = inflater.inflate(layoutId, container, false)
        return mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewInitiated = true
        initView(view)
        prepareRequest()
    }

    private fun prepareRequest() {
        if (isViewInitiated && !isDataInitiated && !isHidden) {
            requestStart()
            isDataInitiated = true
        }
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