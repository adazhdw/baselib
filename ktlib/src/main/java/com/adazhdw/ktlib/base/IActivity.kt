package com.adazhdw.ktlib.base

/**
 * Created by adazhdw on 2020/3/5.
 */
interface IActivity {

    /**
     * 返回布局Id
     */
    val layoutId: Int

    /**
     * 初始化数据
     */
    fun initData(){}

    /**
     * 初始化View
     */
    fun initView(){}

    /**
     * 是否需要EventBus
     */
    fun needEventBus(): Boolean = false

    /**
     * 网络请求开始
     */
    fun requestStart(){}


}