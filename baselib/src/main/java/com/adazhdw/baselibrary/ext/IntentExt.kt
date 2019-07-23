package com.adazhdw.baselibrary.ext

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.FragmentActivity


/**
 * check if there has apps that accept your intent
 * @param context
 * @param action
 * @return
 */
fun FragmentActivity.isIntentAvailable(action: String): Boolean {
    val packageManager = packageManager
    val intent = Intent(action)
    val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return list.size > 0
}


/**
 * open manage battery page
 */
fun FragmentActivity.intentBattery() {
    val intentBatteryUsage = Intent(Intent.ACTION_POWER_USAGE_SUMMARY)
    startActivity(intentBatteryUsage)
}

/**
 * open browser
 */
fun FragmentActivity.intentBrowser(url: String) {
    val viewIntent = Intent("android.intent.action.VIEW", Uri.parse(url))
    startActivity(viewIntent)
}

/**
 * 调用便携式热点和数据共享设置
 */
fun FragmentActivity.getHotspotSetting() {
    val intent = Intent()
    intent.action = Intent.ACTION_MAIN
    val com = ComponentName("com.android.settings", "com.android.seings.TetherSettings")
    intent.component = com
    startActivity(intent)
}

/**
 * start APK‘s default Activity
 */
fun FragmentActivity.startApkActivity(packageName: String) {
    val pm = packageManager
    val pi: PackageInfo
    try {
        pi = pm.getPackageInfo(packageName, 0)
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setPackage(pi.packageName)

        val apps = pm.queryIntentActivities(intent, 0)

        val ri = apps.iterator().next()
        if (ri != null) {
            val className = ri.activityInfo.name
            intent.component = ComponentName(packageName, className)
            startActivity(intent)
        }
    } catch (e: PackageManager.NameNotFoundException) {
        logE(e)
    }
}

/**
 * start application detail page
 */
fun FragmentActivity.launchSettings(code: Int? = null) {
    val intent = Intent()
    intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.parse("package:$packageName")
    if (code != null) {
        startActivityForResult(intent, code)
    } else {
        startActivity(intent)
    }
}




