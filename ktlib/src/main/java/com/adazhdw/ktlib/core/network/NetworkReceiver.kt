package com.adazhdw.ktlib.core.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

open class NetworkReceiver(private val mNetworkListener: NetworkListener? = null) : BroadcastReceiver() {

    private val TAG: String = "NetworkReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        //监听wifi的变化
        /*if (WifiManager.NETWORK_STATE_CHANGED_ACTION == intent?.action) {
            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)
            Log.d(TAG, "wifiState____$wifiState")
            when (wifiState) {
                WifiManager.WIFI_STATE_ENABLED -> {//3
                    Log.d(TAG,"WIFI_STATE_ENABLED")
                }
                WifiManager.WIFI_STATE_ENABLING -> {//2
                    Log.d(TAG,"WIFI_STATE_ENABLING")
                }
                WifiManager.WIFI_STATE_DISABLED -> {//1
                    Log.d(TAG,"WIFI_STATE_DISABLED")
                }
                WifiManager.WIFI_STATE_DISABLING -> {//0
                    Log.d(TAG,"WIFI_STATE_DISABLING")
                }
            }
        }*/

        //监听所有网络的变化
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent?.action) {
            val info: NetworkInfo? = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO)
            if (info != null) {
                if (NetworkInfo.State.CONNECTED == info.state && info.isAvailable) {
                    when (info.type) {
                        ConnectivityManager.TYPE_WIFI -> mNetworkListener?.onNetAvailable(true, KtNetCallback.NetType.WIFI)
                        ConnectivityManager.TYPE_MOBILE -> mNetworkListener?.onNetAvailable(true, KtNetCallback.NetType.MOBILE)
                        else -> mNetworkListener?.onNetAvailable(true, KtNetCallback.NetType.UN_KNOW)
                    }
                } else {
                    when (info.type) {
                        ConnectivityManager.TYPE_WIFI -> mNetworkListener?.onNetAvailable(false, KtNetCallback.NetType.WIFI)
                        ConnectivityManager.TYPE_MOBILE -> mNetworkListener?.onNetAvailable(false, KtNetCallback.NetType.MOBILE)
                        else -> mNetworkListener?.onNetAvailable(false, KtNetCallback.NetType.UN_KNOW)
                    }
                }
            }
        }
    }


    interface NetworkListener {
        fun onNetAvailable(isConnected: Boolean, netType: KtNetCallback.NetType)
    }
}