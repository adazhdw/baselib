package com.adazhdw.ktlib.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adazhdw.ktlib.base.IFragment
import com.adazhdw.ktlib.ext.logD
import org.greenrobot.eventbus.EventBus

/**
 * 1、布局初始化后，才能加载数据
 * 2、界面是否可见后，才能加载数据
 * 3、是否加载过数据，如果加载过，就不重复加载
 */
abstract class BaseFragment : CoroutinesFragment(), IFragment {

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
        "onCreateView".logD(TAG)
        initData()
        mContentView = inflater.inflate(layoutId, container, false)
        return mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "onViewCreated".logD(TAG)
        isViewInitiated = true
        initView(view)
        prepareRequest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        "onDestroyView".logD(TAG)
    }

    private fun prepareRequest() {
        if (isViewInitiated && !isDataInitiated && !isHidden) {
            requestStart()
            isDataInitiated = true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        "onAttach".logD(TAG)
    }

    override fun onDetach() {
        super.onDetach()
        "onDetach".logD(TAG)
    }

    override fun onStart() {
        super.onStart()
        "onStart".logD(TAG)
        if (needEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        "onStop".logD(TAG)
        super.onStop()
        if (needEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onResume() {
        super.onResume()
        "onResume".logD(TAG)
    }

    override fun onPause() {
        super.onPause()
        "onPause".logD(TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "onCreate".logD(TAG)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        "onActivityCreated".logD(TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy".logD(TAG)
    }

}