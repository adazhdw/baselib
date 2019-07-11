package com.adazhdw.baselibrary.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {

    private val myViewModel = BaseViewModelImpl()
    override val coroutineContext: CoroutineContext
        get() = myViewModel.viewModelScope.coroutineContext

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
    abstract fun initView(view: View)

    /**
     * 是否需要EventBus
     */
    open fun needEventBus(): Boolean = false

    /**
     * 网络请求开始
     */
    abstract fun requestStart()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (needEventBus()) {
            EventBus.getDefault().register(this)
        }
        initView(view)
        initData()
        requestStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        myViewModel.viewModelScope.cancel()
        if (needEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

}