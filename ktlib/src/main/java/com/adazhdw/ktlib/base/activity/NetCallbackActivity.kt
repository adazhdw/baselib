package com.adazhdw.ktlib.base.activity

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.adazhdw.ktlib.core.network.NetStateCallback
import com.adazhdw.ktlib.core.network.NetworkReceiver


/**
 * author: daguozhu
 * created on: 2019/11/8 14:11
 * description:
 */

abstract class NetCallbackActivity : CoroutinesActivity(), NetStateCallback {

    /**
     * 是否需要网络监听callback
     */
    protected open fun needNetCallback(): Boolean = false

    private val mConnectivityManager: ConnectivityManager by lazy { getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    private var mNetworkCallback: ConnectivityManager.NetworkCallback? = null
    private var mNetworkReceiver: NetworkReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (needNetCallback()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                registerNetCallback()
            } else {
                registerNetReceiver()
            }
        }
    }

    private fun registerNetReceiver() {
        mNetworkReceiver = NetworkReceiver(mNetworkListener = object :
            NetworkReceiver.NetworkListener {
            override fun onNetAvailable(isConnected: Boolean, netType: NetStateCallback.NetType) {
                if (isConnected) {
                    onNetAvailable(netType, true)
                } else {
                    onNetUnAvailable(netType, true)
                }
            }
        })
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(mNetworkReceiver, intentFilter)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun registerNetCallback() {
        mNetworkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val capabilities: NetworkCapabilities? = mConnectivityManager.getNetworkCapabilities(network)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        onNetAvailable(NetStateCallback.NetType.WIFI)
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        onNetAvailable(NetStateCallback.NetType.MOBILE)
                    }
                } else {
                    onNetAvailable(NetStateCallback.NetType.UN_KNOW)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                val capabilities: NetworkCapabilities? = mConnectivityManager.getNetworkCapabilities(network)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        onNetUnAvailable(NetStateCallback.NetType.WIFI)
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        onNetUnAvailable(NetStateCallback.NetType.MOBILE)
                    }
                } else {
                    onNetUnAvailable(NetStateCallback.NetType.UN_KNOW)
                }
            }

        }
        mConnectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), mNetworkCallback!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needNetCallback()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mNetworkCallback != null)
                    mConnectivityManager.unregisterNetworkCallback(mNetworkCallback!!)
            } else {
                unregisterReceiver(mNetworkReceiver)
            }
        }
    }
}
