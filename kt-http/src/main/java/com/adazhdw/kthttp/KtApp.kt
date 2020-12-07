package com.adazhdw.kthttp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import java.lang.reflect.InvocationTargetException


object KtApp {

    private var currentApplication: Application? = null

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
        } else {
            if (application != null && application.javaClass != currentApplication?.javaClass) {
                currentApplication = application
            }
        }
    }
}
