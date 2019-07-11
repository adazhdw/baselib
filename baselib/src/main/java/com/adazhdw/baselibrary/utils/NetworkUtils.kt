package com.adazhdw.baselibrary.utils

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresPermission
import com.adazhdw.baselibrary.LibUtil

object NetworkUtils {
    val isConnected: Boolean
        @RequiresPermission(ACCESS_NETWORK_STATE)
        get() {
            val info = activeNetworkInfo
            return info != null && info.isConnected
        }


    private val activeNetworkInfo: NetworkInfo?
        @RequiresPermission(ACCESS_NETWORK_STATE)
        get() {
            val cm =
                LibUtil.getApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return cm?.activeNetworkInfo
        }

    val isWifiConnected: Boolean
        get() {
            val connectivityManager =
                LibUtil.getApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
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

    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
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
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
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

}
