package com.adazhdw.ktlib.utils

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresPermission
import com.adazhdw.ktlib.LibUtil

object NetworkUtil {

    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isConnected(): Boolean {
        val info = activeNetworkInfo()
        return info != null && info.isConnected
    }


    @RequiresPermission(ACCESS_NETWORK_STATE)
    private fun activeNetworkInfo(context: Context? = null): NetworkInfo? {
        return getCm(context)?.activeNetworkInfo
    }

    @RequiresPermission(ACCESS_WIFI_STATE)
    fun isWifiConnected(context: Context?=null): Boolean {
        val connectivityManager = getCm(context)
        val networkInfo = connectivityManager?.activeNetworkInfo
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val network: Network? = connectivityManager?.activeNetwork
                val capabilities = connectivityManager?.getNetworkCapabilities(network)
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true && networkInfo?.isConnected == true
            }
            else -> networkInfo?.isConnected ?: false && networkInfo?.type == ConnectivityManager.TYPE_WIFI
        }
    }

    /**
     * 获取网络类型
     */
    fun getNetworkType(context: Context?): String {
        val connectivityManager = getCm(context)
        val info = connectivityManager?.activeNetworkInfo
        when {
            info?.isConnected == true -> return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network: Network? = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                when {
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "WIFI"
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "MOBILE"
                    else -> "TYPE_ELSE"
                }
            } else {
                when (info.type) {
                    ConnectivityManager.TYPE_WIFI -> "WIFI"
                    ConnectivityManager.TYPE_MOBILE -> "MOBILE"
                    else -> "TYPE_ELSE"
                }
            }
            else -> return "NETWORK_DISCONNECT"
        }
    }

    /**
     * 获取网络类型hw
     */
    fun getNetworkTypeHw(context: Context?): Int {
        return getPsType(activeNetworkInfo(context))
    }

    private fun getPsType(netInfo: NetworkInfo?): Int {
        var psType = 0
        if (netInfo != null && netInfo.isConnected) {
            if (netInfo.getType() == 1) {
                psType = 1
            } else if (0 == netInfo.getType()) {
                psType = when (netInfo.subtype) {
                    1, 2, 4 -> 2
                    3, 5, 6, 7, 8, 9, 10, 11, 12, 15 -> 3
                    13, 14 -> 4
                    else -> 6
                }
            }
        }

        return psType
    }

    private fun getCm(context: Context?=null): ConnectivityManager? {
        return if (context != null) {
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        } else {
            LibUtil.getApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        }
    }



    class NetType {
        companion object {
            const val WAP = -3
            const val NET = -2
            const val TYPE_NEED_INIT = -1
            const val TYPE_UNKNOWN = 0
            const val TYPE_WIFI = 1
            const val TYPE_2G = 2
            const val TYPE_3G = 3
            const val TYPE_4G = 4
            const val TYPE_5G = 5
            const val TYPE_OTHER = 6
        }
    }

}
