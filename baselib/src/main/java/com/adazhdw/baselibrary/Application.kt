package com.adazhdw.baselibrary

import android.app.Application
import com.adazhdw.baselibrary.ext.DelegateExt

/**
 * use application implement is better than getApplication() by using reflect(反射)
 */
abstract class Application : Application() {

    companion object {
        //Delegate 单例 委托属性
        var instance by DelegateExt.notNullSingleValue<Application>()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initLibrary(baseUrl(), isDebug())
    }

    abstract fun baseUrl(): String
    open fun isDebug(): Boolean = false
}