package com.adazhdw.ktlib.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.adazhdw.ktlib.base.IFragment
import org.greenrobot.eventbus.EventBus


/**
 * daguozhu
 * create at 2020/4/2 15:53
 * description:
 */
abstract class ViewBindingFragment : CoroutinesFragment(), IFragment {

    /**
     * 是否初始化过布局
     */
    protected var isViewInitiated = false

    /**
     * 是否加载过数据
     */
    protected var isDataInitiated = false

    protected lateinit var mContext: Context
    lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (isViewBinding()) {
            initViewBinding(inflater, container, layoutId).root
        } else {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewInitiated = true
        mContext = view.context
        initView(view)
        initData()
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

    abstract fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        @LayoutRes resId: Int
    ): ViewDataBinding

    open fun isViewBinding(): Boolean = true

    protected inline fun <reified T : ViewDataBinding> binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        @LayoutRes resId: Int
    ): T = DataBindingUtil.inflate(inflater, resId, container, false)

}