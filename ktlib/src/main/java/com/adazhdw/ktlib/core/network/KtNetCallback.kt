package com.adazhdw.ktlib.core.network

/**
 * author: daguozhu
 * created on: 2019/11/8 14:46
 * description:
 */


interface KtNetCallback {
    fun onNetAvailable(netType: NetType, fromReceiver: Boolean = false){}
    fun onNetUnAvailable(netType: NetType, fromReceiver: Boolean = false){}

    enum class NetType {
        WIFI, MOBILE, UN_KNOW
    }
}
