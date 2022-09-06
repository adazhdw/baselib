package com.adazhdw.ktlib.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.adazhdw.ktlib.base.IFragment
import com.adazhdw.ktlib.ext.logD
import org.greenrobot.eventbus.EventBus


/**
 * daguozhu
 * create at 2020/4/2 15:53
 * description:
 */
abstract class ViewBindingFragment<VB : ViewBinding> : CoroutinesFragment(), IFragment {

    override val needEventBus: Boolean
        get() = false

    override val layoutId: Int
        get() = 0

    protected lateinit var viewBinding: VB

    /**
     * 是否初始化过布局
     */
    protected var isViewInitiated = false

    /**
     * 是否加载过数据
     */
    protected var isDataInitiated = false

    lateinit var mContext: Context
    lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        "onAttach".logD(TAG)
        mContext = context
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "onCreate".logD(TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        "onCreateView".logD(TAG)
        viewBinding = inflateViewBinding(inflater, container)
        return viewBinding.root
    }

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "onViewCreated".logD(TAG)
        isViewInitiated = true
        initView(view)
        initData()
        prepareRequest()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        "onActivityCreated".logD(TAG)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        "onViewStateRestored".logD(TAG)
    }

    private fun prepareRequest() {
        if (isViewInitiated && !isDataInitiated && !isHidden) {
            requestStart()
            isDataInitiated = true
        }
    }

    override fun onStart() {
        super.onStart()
        "onStart".logD(TAG)
        if (needEventBus) {
            EventBus.getDefault().register(this)
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

    override fun onStop() {
        "onStop".logD(TAG)
        super.onStop()
        if (needEventBus) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewInitiated = false
        isDataInitiated = false
        "onDestroyView".logD(TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy".logD(TAG)
    }

    override fun onDetach() {
        super.onDetach()
        "onDetach".logD(TAG)
    }

}