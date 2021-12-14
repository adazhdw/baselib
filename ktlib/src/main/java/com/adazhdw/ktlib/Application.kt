package com.adazhdw.ktlib

import android.app.Application

/**
 * use application implement is better than getApplication() by using reflect(反射)
 */
open class Application : Application() {

    protected open val isDebug = false
    override fun onCreate() {
        super.onCreate()
        initLibrary(isDebug)
    }
}