package com.adazhdw.kthttp.util

import android.Manifest.permission
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.adazhdw.kthttp.KtApp

/**
 * author：daguozhu
 * date-time：2020/11/16 18:00
 * description：
 **/

object NetworkUtil {

    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isConnected(): Boolean {
        val cm = getConnectivityManager() ?: return false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return isConnectedOver23(capabilities)
        } else {
            val networkInfo = cm.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isMobile(): Boolean {
        val cm = getConnectivityManager() ?: return false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return isConnectedOver23(capabilities) && isMobileOver21(capabilities)
        } else {
            val networkInfo = cm.activeNetworkInfo ?: return false
            return networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_MOBILE
        }
    }

    @RequiresApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    private fun isMobileOver21(capabilities: NetworkCapabilities): Boolean {
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)//蜂窝网络
    }

    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isWifi(): Boolean {
        val cm = getConnectivityManager() ?: return false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return isConnectedOver23(capabilities) && isWifiOver21(capabilities)
        } else {
            val networkInfo = cm.activeNetworkInfo ?: return false
            return networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI
        }
    }

    @RequiresApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    private fun isWifiOver21(capabilities: NetworkCapabilities): Boolean {
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun wifiEnabled(): Boolean {
        val wm = getWifiManager() ?: return false
        return wm.isWifiEnabled
    }

    @RequiresApi(android.os.Build.VERSION_CODES.Q)
    @RequiresPermission(permission.CHANGE_WIFI_STATE)
    fun setWifiEnabled(enabled: Boolean) {
        val wm = getWifiManager() ?: return
        wm.isWifiEnabled = enabled
    }

    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isBluetooth(): Boolean {
        val cm = getConnectivityManager() ?: return false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return isConnectedOver23(capabilities) && isBluetoothOver21(capabilities)
        } else {
            val networkInfo = cm.activeNetworkInfo ?: return false
            return networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_BLUETOOTH
        }
    }

    @RequiresApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    private fun isBluetoothOver21(capabilities: NetworkCapabilities): Boolean {
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
    }

    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isEthernet(): Boolean {
        val cm = getConnectivityManager() ?: return false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return isConnectedOver23(capabilities) && isEthernetOver21(capabilities)
        } else {
            val networkInfo = cm.activeNetworkInfo ?: return false
            return networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_ETHERNET
        }
    }

    @RequiresApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    private fun isEthernetOver21(capabilities: NetworkCapabilities): Boolean {
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    @RequiresApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isVPN(): Boolean {
        val cm = getConnectivityManager() ?: return false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return isConnectedOver23(capabilities) && isVPNOver21(capabilities)
        } else {
            val networkInfo = cm.activeNetworkInfo ?: return false
            return networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_VPN
        }
    }

    @RequiresApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    private fun isVPNOver21(capabilities: NetworkCapabilities): Boolean {
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }

    /**
     *
     */
    @RequiresApi(android.os.Build.VERSION_CODES.M)
    fun isConnectedOver23(capabilities: NetworkCapabilities): Boolean {
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private fun getConnectivityManager(): ConnectivityManager? {
        return KtApp.context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private fun getWifiManager(): WifiManager? {
        return KtApp.getApp().applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
    }

}