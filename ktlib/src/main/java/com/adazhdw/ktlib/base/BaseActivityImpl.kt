package com.adazhdw.ktlib.base

import com.adazhdw.ktlib.core.network.KtNetCallback

abstract class BaseActivityImpl :BaseActivity() {

    override fun initView() {

    }

    override fun initData() {

    }

    override fun requestStart() {

    }

    override fun onNetAvailable(netType: KtNetCallback.NetType, fromReceiver: Boolean) {

    }

    override fun onNetUnAvailable(netType: KtNetCallback.NetType, fromReceiver: Boolean) {

    }

}