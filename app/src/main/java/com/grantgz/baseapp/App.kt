package com.grantgz.baseapp

import com.adazhdw.baselibrary.Application

class App : Application() {
    override fun baseUrl(): String = BuildConfig.DOMAIN

    override fun isDebug(): Boolean = true
}