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
        logD(TAG, "onCreated: " + activity.componentName.className)
    }

    override fun onActivityStarted(activity: Activity) {
        logD(TAG, "onStarted: " + activity.componentName.className)
    }

    override fun onActivityResumed(activity: Activity) {
        logD(TAG, "onResumed: " + activity.componentName.className)
    }

    override fun onActivityPaused(activity: Activity) {
        logD(TAG, "onPaused: " + activity.componentName.className)
    }

    override fun onActivityStopped(activity: Activity) {
        logD(TAG, "onStopped: " + activity.componentName.className)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        logD(TAG, "onActivitySaveInstanceState: " + activity.componentName.className)
    }

    override fun onActivityDestroyed(activity: Activity) {
        logD(TAG, "onDestroyed: " + activity.componentName.className)
    }
}