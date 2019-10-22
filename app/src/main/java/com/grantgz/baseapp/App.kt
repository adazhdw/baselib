package com.grantgz.baseapp

import com.adazhdw.ktlib.Application
import com.adazhdw.ktlib.delegate.DelegateExt
import com.adazhdw.ktlib.ext.logD

class App : Application() {

    companion object {
        //Delegate 单例 委托属性
        var context by DelegateExt.notNullSingleValue<App>()
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        context.packageName.logD()
    }

    override fun baseUrl(): String = BuildConfig.DOMAIN

    override fun isDebug(): Boolean = true
}