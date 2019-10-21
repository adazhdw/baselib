package com.adazhdw.baselibrary.core.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.adazhdw.baselibrary.LibUtil
import com.adazhdw.baselibrary.ext.logD


/**
 * author: daguozhu
 * created on: 2019/10/18 10:50
 * description:
 */

class KtLifecycleCallback : Application.ActivityLifecycleCallbacks {
    private val TAG by lazy { LibUtil.getApp().packageName }
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ("onCreated: " + activity.componentName.className).logD(TAG)
    }

    override fun onActivityStarted(activity: Activity) {
        ("onStarted: " + activity.componentName.className).logD(TAG)
    }

    override fun onActivityResumed(activity: Activity) {
        ("onResumed: " + activity.componentName.className).logD(TAG)
    }

    override fun onActivityPaused(activity: Activity) {
        ("onPaused: " + activity.componentName.className).logD(TAG)
    }

    override fun onActivityStopped(activity: Activity) {
        ("onStopped: " + activity.componentName.className).logD(TAG)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        ("onActivitySaveInstanceState: " + activity.componentName.className).logD(TAG)
    }

    override fun onActivityDestroyed(activity: Activity) {
        ("onDestroyed: " + activity.componentName.className).logD(TAG)
    }
}