package com.adazhdw.ktlib.utils

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.adazhdw.ktlib.KtLib
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 * author：daguozhu
 * date-time：2020/11/16 18:00
 * description：
 **/

object NetworkUtil {

    /**
     * Open the settings of wireless.
     */
    fun openWirelessSettings() {
        KtLib.getApp().startActivity(
            Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

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
        return KtLib.context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private fun getWifiManager(): WifiManager? {
        return KtLib.getApp().applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
    }

    /**
     * Return the ip address.
     *
     * Must hold `<uses-permission android:name="android.permission.INTERNET" />`
     *
     * @param useIPv4 True to use ipv4, false otherwise.
     * @return the ip address
     */
    @RequiresPermission(permission.INTERNET)
    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            val adds = LinkedList<InetAddress>()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                if (!ni.isUp || ni.isLoopback) {
                    continue
                }
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement())
                }
            }
            for (add in adds) {
                if (!add.isLoopbackAddress) {
                    val hostAddress = add.hostAddress
                    val isIPv4 = hostAddress.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) {
                            return hostAddress
                        }
                    } else {
                        if (!isIPv4) {
                            val index = hostAddress.indexOf('%')
                            return if (index < 0) {
                                hostAddress.toUpperCase(Locale.getDefault())
                            } else hostAddress.substring(0, index).toUpperCase(
                                Locale.getDefault()
                            )
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return ""
    }
}