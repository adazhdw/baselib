package com.adazhdw.baselibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.adazhdw.baselibrary.ext.logD
import com.adazhdw.baselibrary.http.RetrofitModel
import com.adazhdw.baselibrary.utils.SPUtils
import com.blankj.utilcode.util.Utils
import java.lang.reflect.InvocationTargetException

object LibUtil {

    private var currentApplication: Application? = null
    private const val TAG = "com.adazhdw.baselibrary"

    fun getApp(): Application {
        if (currentApplication != null) return currentApplication!!
        val app = getAppByReflect()
        init(app)
        return app
    }

    private fun getAppByReflect(): Application {
        try {
            @SuppressLint("PrivateApi")
            val activityThread = Class.forName("android.app.ActivityThread")
            val thread = activityThread.getMethod("currentActivityThread").invoke(null)
            val app = activityThread.getMethod("getApplication").invoke(thread)
                ?: throw NullPointerException("u should init first")
            return app as Application
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        throw NullPointerException("u should init first")
    }

    fun init(application: Application?) {
        if (currentApplication == null) {
            currentApplication = application ?: getAppByReflect()
            currentApplication?.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        } else {
            if (application != null && application.javaClass != currentApplication?.javaClass) {
                currentApplication?.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
                currentApplication = application
                currentApplication?.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
            }
        }
    }


    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
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
}

/**
 * 初始化 Utils工具包 和 BaseUrl
 */
fun Application.initLibrary(baseUrl: String, debug: Boolean = false) {
    Utils.init(this)
    RetrofitModel.initBaseUrl(baseUrl)
    LibUtil.init(this)
    isDebug = debug
}

internal var isDebug = false
