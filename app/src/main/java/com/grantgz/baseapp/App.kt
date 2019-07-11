package com.grantgz.baseapp

import com.adazhdw.baselibrary.Application

class App : Application() {
    override fun baseUrl(): String = "https://wanandroid.com"

    override fun isDebug(): Boolean = true
}