package com.adazhdw.baselibrary.ext

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import java.io.File

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
fun Context.installApk(file: File) {
    startActivity(Intent().apply {
        action = "android.intent.action.VIEW"
        addCategory("android.intent.category.DEFAULT")
        type = "application/vnd.android.package-archive"
        setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

/**
 * 获取应用名称
 */
fun Context.getApkName(packageId: String): String {
    try {
        return packageManager.getPackageInfo(packageId, 0).applicationInfo.loadLabel(packageManager).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}

fun Context.getVersionName(): String? {
    return packageManager.getPackageInfo(packageName, 0).versionName
}


