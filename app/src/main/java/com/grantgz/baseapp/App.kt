package com.grantgz.baseapp

import com.adazhdw.baselibrary.Application
import com.adazhdw.baselibrary.ext.DelegateExt
import com.adazhdw.baselibrary.ext.logD

class App : Application() {

    companion object {
        //Delegate 单例 委托属性
        var context by DelegateExt.notNullSingleValue<App>()
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        logD(context.packageName)
    }

    override fun baseUrl(): String = BuildConfig.DOMAIN

    override fun isDebug(): Boolean = true
}