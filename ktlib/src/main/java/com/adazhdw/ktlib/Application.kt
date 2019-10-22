package com.adazhdw.ktlib

import android.app.Application

/**
 * use application implement is better than getApplication() by using reflect(反射)
 */
open class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initLibrary(baseUrl(), isDebug())
    }

    open fun baseUrl(): String = ""
    open fun isDebug(): Boolean = false
}