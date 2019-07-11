package com.adazhdw.baselibrary

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * use application implement is better than getApplication() by using reflect(反射)
 */
open class Application : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        initLibrary(baseUrl(), isDebug())
    }

    open fun baseUrl(): String = ""
    open fun isDebug(): Boolean = false
}