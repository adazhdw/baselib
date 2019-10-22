package com.adazhdw.ktlib

import android.annotation.SuppressLint
import android.app.Application
import com.adazhdw.ktlib.core.lifecycle.KtLifecycleCallback

import com.adazhdw.ktlib.http.RetrofitModel
import com.blankj.utilcode.util.Utils
import java.lang.reflect.InvocationTargetException

object LibUtil {

    private var currentApplication: Application? = null
    private val mKtLifecycleCallback = KtLifecycleCallback()

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
    Utils.init(this)
    RetrofitModel.initBaseUrl(baseUrl)
    LibUtil.init(this)
    isDebug = debug
}

internal var isDebug = false
