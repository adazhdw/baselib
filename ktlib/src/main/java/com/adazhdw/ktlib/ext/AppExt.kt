package com.adazhdw.ktlib.ext

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import java.io.File


val Context.versionName: String
    get() = packageManager.getPackageInfo(packageName, 0).versionName

val Context.versionCode: Long
    get() = with(packageManager.getPackageInfo(packageName, 0)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) longVersionCode else versionCode.toLong()
    }


/**
 * 获取应用程序下所有Activity
 *
 **/
fun Context.getAllActivities(): List<String> {
    return packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN, null).apply {
        setPackage(packageName)
    }, 0).map { it.activityInfo.name }
}

/**
 * 获取设备上已安装并且可启动的应用列表
 */
fun Context.getLaunchApps(): List<ResolveInfo> {
    return packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }, 0)
}

/**
 * 主动回到home
 */
fun Context.goHome() {
    startActivity(Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
    })
}

/**
 * 安装apk
 */
private fun Context.installApk(file: File) {
    val uri: Uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Uri.fromFile(file)
    } else {
        FileProvider.getUriForFile(this, "packageName.fileprovider", file)
    }
    startActivity(Intent().apply {
        action = "android.intent.action.VIEW"
        addCategory("android.intent.category.DEFAULT")
        type = "application/vnd.android.package-archive"
        setDataAndType(uri, "application/vnd.android.package-archive")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    })
}

/**
 * 获取应用名称
 */
fun Context.getApkName(packageId: String): String {
    try {
        return packageManager.getPackageInfo(packageId, 0).applicationInfo.loadLabel(packageManager)
            .toString()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}

/**
 * check if there has apps that accept your intent
 * @param action
 * @return
 */
fun Context.isIntentAvailable(action: String): Boolean {
    val packageManager = packageManager
    val intent = Intent(action)
    val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return list.size > 0
}


/**
 * open manage battery page
 */
fun Context.jumpBattery() {
    val intentBatteryUsage = Intent(Intent.ACTION_POWER_USAGE_SUMMARY)
    startActivity(intentBatteryUsage)
}

/**
 * open browser
 */
fun Context.jumpBrowser(url: String) {
    val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(viewIntent)
}

/**
 * 调用便携式热点和数据共享设置
 */
fun Context.jumpHotSpots() {
    val intent = Intent()
    intent.action = Intent.ACTION_MAIN
    val com = ComponentName("com.android.settings", "com.android.seings.TetherSettings")
    intent.component = com
    startActivity(intent)
}

/**
 * start APK‘s default Activity
 */
fun Context.startApkActivity(packageName: String) {
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
        e.message?.logE()
    }
}

/**
 * start application detail page
 */
fun FragmentActivity.jumpSettings(code: Int? = null) {
    val intent = Intent()
    intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.parse("package:$packageName")
    if (code != null) {
        startActivityForResult(intent, code)
    } else {
        startActivity(intent)
    }
}





