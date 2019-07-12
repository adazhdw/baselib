package com.grantgz.baseapp

import com.adazhdw.baselibrary.Application
import com.adazhdw.baselibrary.ext.DelegateExt
import com.adazhdw.baselibrary.ext.logD

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        logD(instance.packageName)
    }

    override fun baseUrl(): String = BuildConfig.DOMAIN

    override fun isDebug(): Boolean = true
}