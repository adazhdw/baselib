package com.adazhdw.baselibrary

import android.app.Application
import com.adazhdw.baselibrary.ext.DelegateExt

/**
 * use application implement is better than getApplication() by using reflect(反射)
 */
open class Application : Application() {

    companion object {
        //单例委托
        var instance: com.adazhdw.baselibrary.Application by DelegateExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initLibrary(baseUrl(), isDebug())
    }

    open fun baseUrl(): String = ""
    open fun isDebug(): Boolean = false
}