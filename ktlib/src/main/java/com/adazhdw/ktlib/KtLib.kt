package com.adazhdw.ktlib

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.adazhdw.ktlib.core.lifecycle.KtLifecycleCallback
import java.lang.reflect.InvocationTargetException


object KtLib {

    private var currentApplication: Application? = null
    private val mKtLifecycleCallback = KtLifecycleCallback()
    internal var isDebug = true
    internal var mBaseUrl = ""

    val context: Context
        get() = getApp()

    fun getApp(): Context {
        if (currentApplication != null) return currentApplication!!.applicationContext
        val app = getAppByReflect()
        init(app)
        return app.applicationContext
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
            currentApplication?.registerActivityLifecycleCallbacks(mKtLifecycleCallback)
        } else {
            if (application != null && application.javaClass != currentApplication?.javaClass) {
                currentApplication?.unregisterActivityLifecycleCallbacks(mKtLifecycleCallback)
                currentApplication = application
                currentApplication?.registerActivityLifecycleCallbacks(mKtLifecycleCallback)
            }
        }
    }
}

/**
 * 初始化 Utils工具包 和 BaseUrl
 */
fun Application.initLibrary(baseUrl: String, debug: Boolean = false) {
    KtLib.init(this)
    KtLib.mBaseUrl = baseUrl
    KtLib.isDebug = debug
}
