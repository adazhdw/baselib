package com.adazhdw.ktlib.base

import com.adazhdw.ktlib.core.network.KtNetCallback

@Deprecated("use BaseActivity")
abstract class BaseActivityImpl :BaseActivity() {

    override fun onNetAvailable(netType: KtNetCallback.NetType, fromReceiver: Boolean) {

    }

    override fun onNetUnAvailable(netType: KtNetCallback.NetType, fromReceiver: Boolean) {

    }

}