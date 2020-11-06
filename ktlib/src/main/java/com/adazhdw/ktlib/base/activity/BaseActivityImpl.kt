package com.adazhdw.ktlib.base.activity

import com.adazhdw.ktlib.core.network.NetStateCallback

@Deprecated("use BaseActivity")
abstract class BaseActivityImpl : BaseActivity() {

    override fun onNetAvailable(netType: NetStateCallback.NetType, fromReceiver: Boolean) {

    }

    override fun onNetUnAvailable(netType: NetStateCallback.NetType, fromReceiver: Boolean) {

    }

}